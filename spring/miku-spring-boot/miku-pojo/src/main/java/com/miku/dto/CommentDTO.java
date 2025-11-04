package com.miku.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.java.Log;

import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {

    //评论所属id
    private Long articleId;

    //用户id
    @JsonProperty("uid")
    private Long userId;

    //如果是回复评论,提供父评论的ID,如果是一级评论,则为null
    private Long parentId;

    //评论内容
    private String content;
}
