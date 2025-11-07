package com.miku.service.Impl;

import com.miku.context.BaseContext;
import com.miku.dto.CommentDTO;
import com.miku.entity.Comment;
import com.miku.mapper.CommentMapper;
import com.miku.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 提交评论
     * @param commentDTO
     * @return
     */
    @Override
    public CommentDTO putComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        //对象拷贝
        BeanUtils.copyProperties(commentDTO, comment);

        Long userId = BaseContext.getCurrentId();

        //设置发布和修改时间
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        comment.setUserId(userId);
        commentMapper.insert(comment);
        commentDTO.setId(comment.getId());
        return commentDTO;

    }
}
