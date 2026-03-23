package com.miku.controller;

import com.miku.context.BaseContext;
import com.miku.dto.CommentDTO;
import com.miku.dto.PageQueryDTO;
import com.miku.result.PageResult;
import com.miku.result.Result;
import com.miku.service.CommentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 提交评论
     * @param commentDTO
     * @return
     */
    @PostMapping("/submit")
    public Result<CommentDTO> putComment(@Valid @RequestBody CommentDTO commentDTO){
        log.info("提交评论:{}",commentDTO);
        CommentDTO comment = commentService.putComment(commentDTO);
        return Result.success(comment);
    }

    /**
     * 获取评论列表
     * @param articleId
     * @return
     */
    @GetMapping("/list/{articleId}")
    public Result<PageResult> getCommentList(@PathVariable Long articleId, PageQueryDTO pageQueryDTO){
        log.info("获取评论列表:{}",articleId);
        PageResult pageResult = commentService.getCommentList(articleId,pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据评论表主键id删除评论
     * @param id
     * @return
     */
    @PostMapping("/delete/{commentId}")
    public Result deleteComment(@PathVariable("commentId") Long id){
        log.info("删除评论:{}",id);
        Long userId = BaseContext.getCurrentId();
        boolean success = commentService.delectComment(id, userId);
        if (success){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
