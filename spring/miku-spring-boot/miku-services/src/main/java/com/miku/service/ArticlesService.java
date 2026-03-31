package com.miku.service;

import com.miku.dto.CursorPageQueryDTO;
import com.miku.dto.CreateArticlesDTO;
import com.miku.result.CursorPageResult;
import com.miku.result.PageResult;
import com.miku.vo.ArticleDetailVO;

public interface ArticlesService {
    /**
     * 文章列表查询
     *
     * @param cursorPageQueryDTO
     * @return
     */
    CursorPageResult pageQuery(CursorPageQueryDTO cursorPageQueryDTO);

    /**
     * 获取文章详细页
     * @param id
     * @return
     */
    ArticleDetailVO getArticleDetail(Long id, String ip);

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
