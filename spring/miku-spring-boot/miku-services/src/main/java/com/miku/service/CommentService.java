package com.miku.service;

import com.miku.dto.CommentDTO;

public interface CommentService {

    /**
     * 提交评论
     * @param commentDTO
     */
    CommentDTO putComment(CommentDTO commentDTO);
}
