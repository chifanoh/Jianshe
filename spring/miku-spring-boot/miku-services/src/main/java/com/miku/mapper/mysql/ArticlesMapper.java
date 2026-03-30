package com.miku.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miku.dto.PageQueryDTO;
import com.miku.entity.Articles;
import com.miku.vo.ArticlesVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticlesMapper extends BaseMapper<Articles> {
    /**
     * 文章列表查询
     * @param page
     * @param pageSize
     * @return
     */
    List<ArticlesVO> selectArticlesList(int page, int pageSize);
}
