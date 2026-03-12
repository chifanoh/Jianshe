package com.miku.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 评论表
* @TableName comment
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

    /**
    * 评论ID
    */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
    * 评论用户ID
    */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
    * 文章ID
    */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;

    /**
    * 父评论ID，用于回复
    */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /**
    * 评论内容
    */
    private String content;

    /**
    * 评论时间
    */
    private LocalDateTime createTime;

    /**
    * 更新时间
    */
    private LocalDateTime updateTime;

    /**
    * 点赞数
    */
    private Integer likes;

    /**
    * 评论状态 (0: 正常, 1: 待审核, 2: 已删除)
    */
    private Integer status;
}
