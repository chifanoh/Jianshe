package com.miku.service;

import com.miku.dto.PageQueryDTO;
import com.miku.dto.CreateArticlesDTO;
import com.miku.result.PageResult;
import com.miku.vo.ArticleDetailVO;

public interface ArticlesService {
    /**
     * 文章查询
     * @param pageQueryDTO
     * @return
     */
    PageResult pageQuery(PageQueryDTO pageQueryDTO);

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
    void createArtics(String uid, CreateArticlesDTO createArticlesDTO);
}
