package com.miku.service.Impl;

import com.miku.dto.SearchDTO;
import com.miku.entity.ManticoreArticle;
import com.miku.entity.User;
import com.miku.mapper.manticore.ArticlesSearchMapper;
import com.miku.mapper.mysql.UserMapper;
import com.miku.result.PageResult;
import com.miku.service.SearchService;
import com.miku.vo.ManticoreArticleVO;
import com.miku.vo.UserArticlesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ArticlesSearchMapper articlesSearchMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 文章搜索
     * @param searchDTO
     * @return
     */
    @Override
    public PageResult search(SearchDTO searchDTO) {
        // 查询数据
        int page = (searchDTO.getPage() - 1) * searchDTO.getPageSize(); // 计算偏移值
        List<ManticoreArticle> articles = articlesSearchMapper.searchArticles(
                searchDTO.getKeyword(),
                page,
                searchDTO.getPageSize()
        );

        // 如果没有文章，直接返回
        if (articles.isEmpty()) {
            return null;
        }

        // 收集userid，分开查询后组装
        List<Long> userId = articles.stream()
                .filter(article -> article != null && article.getUserId() != null)
                .map(ManticoreArticle::getUserId)
                .collect(Collectors.toList());

        // 查询user表
        List<User> userList = userMapper.selectBatchIds(userId);
        // 把用户转成 Map<Integer, User> 方便后面取
        Map<Long,User> userMap = new HashMap<>();
        for (User u : userList) {
            userMap.put(u.getId(), u);
        }

        // 拼接数据
        List<ManticoreArticleVO> articlesVO = articles.stream()
                .map(po -> {
                    ManticoreArticleVO vo = new ManticoreArticleVO();
                    vo.setId(po.getId());
                    vo.setTitle(po.getTitle());
                    vo.setContent(po.getContent());
                    vo.setComments(po.getComments());
                    vo.setLikes(po.getLikesCount());
                    vo.setCreatedTime(LocalDateTime.ofEpochSecond(po.getCreatedTime(), 0, ZoneOffset.ofHours(8)));

                    // 设置用户信息
                    User u = userMap.get(po.getUserId());
                    vo.setUser(UserArticlesVO.builder()
                            .id(u.getId())
                            .name(u.getName())
                            .avatar(u.getAvatar())
                            .build());
                    return vo;
                })
                .collect(Collectors.toList());

        // 统计总数
        int total = articlesSearchMapper.countSearchArticles(searchDTO.getKeyword());
        return new PageResult(total, articlesVO);
    }
}