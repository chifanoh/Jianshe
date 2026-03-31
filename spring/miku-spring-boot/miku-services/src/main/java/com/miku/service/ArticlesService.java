package com.miku.service;

import com.miku.dto.PageQueryDTO;
import com.miku.dto.CreateArticlesDTO;
import com.miku.result.PageResult;
import com.miku.vo.ArticleDetailVO;

public interface ArticlesService {
    /**
     * 文章列表查询
     * @param pageQueryDTO
     * @return
     */
    PageResult pageQuery(PageQueryDTO pageQueryDTO);

    /**
     * 获取文章详细页
     * @param id
     * @return
     */
    ArticleDetailVO getArticleDetail(Long id, Long userId);

    /**
     * 发布文章
     * @param uid
     */
    void createArtics(Long uid, CreateArticlesDTO createArticlesDTO);

    /**
     * 获取点赞信息
     * @param userId
     * @param articleId
     * @return
     */
    boolean getLikeStatus(Long userId, Long articleId);

    /**
     * 点赞
     * @param userId
     * @param articleId
     */
    void toggleLike(Long userId, Long articleId);

}
