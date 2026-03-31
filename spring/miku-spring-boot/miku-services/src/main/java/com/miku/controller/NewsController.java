package com.miku.controller;

import com.miku.dto.NewsQueryDTO;
import com.miku.dto.NewsResultDTO;
import com.miku.result.Result;
import com.miku.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@Slf4j
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * 获取新闻列表
     * @param queryDTO 查询参数
     * @return 新闻结果
     */
    @GetMapping("/list")
    public Result<NewsResultDTO> getNewsList(NewsQueryDTO queryDTO) {
        log.info("获取新闻列表,分类:{},数量:{}", queryDTO.getCategory(), queryDTO.getPageSize());
        NewsResultDTO result = newsService.getNews(queryDTO);
        return Result.success(result);
    }
}