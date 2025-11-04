package com.miku.controller;

import com.miku.dto.CommentDTO;
import com.miku.entity.Comment;
import com.miku.result.Result;
import com.miku.service.CommentService;
import com.miku.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@Slf4j
@CrossOrigin(origins = {"http://localhost:5173"},allowCredentials = "true")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 提交评论
     * @param commentDTO
     * @return
     */
    @PostMapping("/submit")
    public Result<CommentDTO> putComment(@RequestBody CommentDTO commentDTO){
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
    public Result<CommentVO> getCommentList(@PathVariable Long articleId){
        log.info("获取评论列表:{}",articleId);
        return Result.success();
    }
}
