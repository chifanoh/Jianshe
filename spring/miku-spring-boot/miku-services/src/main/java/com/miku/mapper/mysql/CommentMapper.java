package com.miku.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miku.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
