package com.miku.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(type = IdType.INPUT)
    private Long articleId;

    /**
     * 用户ID
     */
    @TableField()
    private Long userId;

    /**
     * 点赞时间
     */
    private LocalDateTime createdTime;
}
