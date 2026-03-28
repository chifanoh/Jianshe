package com.miku.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 评论用户ID
    */
    private Long userId;

    /**
    * 文章ID
    */
    private Long articleId;

    /**
    * 父评论ID，用于回复
    */
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
    private Integer likes_count;

    /**
    * 评论状态 (0: 正常, 1: 待审核, 2: 已删除)
    */
    private Integer status;
}
