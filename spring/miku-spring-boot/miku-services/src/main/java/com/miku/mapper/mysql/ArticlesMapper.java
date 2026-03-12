package com.miku.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miku.dto.PageQueryDTO;
import com.miku.entity.Articles;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticlesMapper extends BaseMapper<Articles> {
    /**
     * 文章列表查询
     * @param pageQueryDTO
     */
    void selectArticlesList(PageQueryDTO pageQueryDTO);
}
