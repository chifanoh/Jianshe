package com.miku.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miku.dto.NewsItem;
import com.miku.dto.NewsQueryDTO;
import com.miku.dto.NewsResultDTO;
import com.miku.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.JsonNode;

@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${news.api.key}")
    private String newsApiKey;

    private static final String REDIS_KEY_PREFIX = "news:";
    private static final long CACHE_EXPIRE_HOURS = 12;
    private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines";

    @Override
    public NewsResultDTO getNews(NewsQueryDTO queryDTO) {
        String category = queryDTO.getCategory() != null ? queryDTO.getCategory() : "general";
        int pageSize = queryDTO.getPageSize();
        String cacheKey = REDIS_KEY_PREFIX + category + ":" + pageSize;

        // 1. 优先从Redis获取缓存（直接返回缓存结果，避免重复解析）
        String cachedData = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cachedData != null) {
            try {
                List<NewsItem> newsItems = objectMapper.readValue(cachedData, new TypeReference<List<NewsItem>>() {});
                return buildResult(newsItems, true);
            } catch (Exception e) {
                log.error("解析缓存数据失败: {}", e.getMessage());
            }
        }

        // 2. 缓存未命中，调用NewsAPI
        String apiResponse = fetchNewsFromApi(category, pageSize);
        if (apiResponse == null) {
            return buildResult(null, false);
        }

        // 3. 解析API响应并缓存
        List<NewsItem> newsItems = parseAndCacheNews(apiResponse, cacheKey);
        return buildResult(newsItems, false);
    }

    private String fetchNewsFromApi(String category, int pageSize) {
        try {
            String url = NEWS_API_URL + "?category=" + category + "&pageSize=" + pageSize + "&apiKey=" + newsApiKey;
            
            // 简化HTTP请求，减少对象创建
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, 
                new HttpEntity<>(headers), String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            log.error("NewsAPI调用失败，状态码: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("调用NewsAPI失败: {}", e.getMessage());
        }
        return null;
    }

    private List<NewsItem> parseAndCacheNews(String apiResponse, String cacheKey) {
        try {
            // 一次性完成解析和缓存，减少JSON操作
            List<NewsItem> newsItems = parseNewsResponse(apiResponse);
            if (newsItems != null && !newsItems.isEmpty()) {
                // 直接使用原始JSON进行缓存，避免重复序列化
                stringRedisTemplate.opsForValue().set(cacheKey, apiResponse, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
                return newsItems;
            }
        } catch (Exception e) {
            log.error("解析或缓存新闻失败: {}", e.getMessage());
        }
        return null;
    }

    private List<NewsItem> parseNewsResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode articles = root.get("articles");
            if (articles == null || !articles.isArray()) return null;
            
            // 使用预分配大小的ArrayList，避免扩容开销
            List<NewsItem> newsItems = new ArrayList<>(articles.size());
            
            for (JsonNode article : articles) {
                NewsItem item = new NewsItem();
                // 简化字段检查，使用get().asText("")避免空检查
                item.setTitle(article.get("title").asText(""));
                item.setDescription(article.get("description").asText(""));
                item.setUrl(article.get("url").asText(""));
                item.setImageUrl(article.get("urlToImage").asText(""));
                item.setPublishedAt(article.get("publishedAt").asText(""));
                item.setSource(article.get("source").get("name").asText(""));
                newsItems.add(item);
            }
            return newsItems;
        } catch (Exception e) {
            log.error("解析NewsAPI响应失败: {}", e.getMessage());
            return null;
        }
    }



    private NewsResultDTO buildResult(List<NewsItem> newsItems, boolean fromCache) {
        NewsResultDTO result = new NewsResultDTO();
        result.setRecords(newsItems);
        result.setCacheTime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        result.setFromCache(fromCache);
        return result;
    }
}