package com.miku.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.extern.java.Log;

import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {

    private Long id;

    //评论所属id
    private Long articleId;

    //用户id
    @JsonProperty("uid")
    private Long userId;

    //如果是回复评论,提供父评论的ID,如果是一级评论,则为null
    private Long parentId;

    //评论内容
    @NotBlank(message = "内容不能为空")
    @Size(message = "内容不能超过{max}个字符", max = 50)
    private String content;
}
