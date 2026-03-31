package com.miku.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miku.entity.Articles;
import com.miku.vo.ArticlesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticlesMapper extends BaseMapper<Articles> {
    /**
     * 文章列表查询
     * @param cursor 游标
     * @param pageSize 每页大小
     * @param direction 排序方向
     * @return
     */
    List<ArticlesVO> selectArticlesListByCursor(@Param("cursor") Long cursor, 
                                               @Param("pageSize") int pageSize,
                                               @Param("direction") String direction);
}