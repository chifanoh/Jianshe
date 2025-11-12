package com.miku.service;

import com.miku.dto.CommentDTO;
import com.miku.dto.PageQueryDTO;
import com.miku.result.PageResult;
import com.miku.vo.CommentVO;

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
}
