package com.miku.service;

import com.miku.dto.ArticlesPageQueryDTO;
import com.miku.dto.CreateArticlesDTO;
import com.miku.result.PageResult;
import com.miku.vo.ArticleDetailVO;

public interface ArticlesService {
    /**
     * 文章查询
     * @param articlesPageQueryDTO
     * @return
     */
    PageResult pageQuery(ArticlesPageQueryDTO articlesPageQueryDTO);

    /**
     * 获取文章详细页
     * @param id
     * @return
     */
    ArticleDetailVO getArticleDetail(Integer id);

    /**
     * 发布文章
     * @param uid
     */
    void createArtics(Long uid, CreateArticlesDTO createArticlesDTO);
}
