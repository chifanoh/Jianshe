package com.miku.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 点赞表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLike implements Serializable {

    /**
     * 文章ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 点赞时间
     */
    private LocalDateTime createdTime;
}
