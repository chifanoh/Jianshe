package com.miku.mapper.manticore;

import com.miku.entity.ManticoreArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchMapper {
    /**
     * 搜索
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    List<ManticoreArticle> searchArticles(String keyword, int page, int pageSize);

    /**
     * 统计总数
     * @param keyword
     * @return
     */
    int countSearchArticles(String keyword);

    /**
     * 双写同步数据库文章
     * @param manticoreArticle
     */
    void insert(ManticoreArticle manticoreArticle);
}