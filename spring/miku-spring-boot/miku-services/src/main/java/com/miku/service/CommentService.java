package com.miku.service;

import com.miku.dto.CommentDTO;
import com.miku.dto.PageQueryDTO;
import com.miku.result.PageResult;

public interface CommentService {

    /**
     * 提交评论
     * @param commentDTO
     */
    CommentDTO putComment(CommentDTO commentDTO);

    /**
     *获取评论列表
     * @param articleId
     * @return
     */
    PageResult getCommentList(Long articleId, PageQueryDTO pageQueryDTO);

    /**
     * 根据评论表主键id删除评论
     *
     * @param id
     * @param userId
     * @return
     */
    boolean delectComment(Long id, Long userId);
}
