package com.miku.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {

    private String id;

    //评论所属id
    private String articleId;

    //用户id
    @JsonProperty("uid")
    private Long userId;

    //如果是回复评论,提供父评论的ID,如果是一级评论,则为null
    private Long parentId;

    //评论内容
    @NotBlank(message = "内容不能为空")
    @Size(message = "内容不能超过50个字符", max = 101)
    private String content;
}
