package com.miku.mapper.search;

import com.miku.vo.ArticlesVO;
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
    List<ArticlesVO> searchArticles(String keyword, int page, int pageSize);

    /**
     * 统计总数
     * @param keyword
     * @return
     */
    int countSearchArticles(String keyword);
}
