package com.miku.entity;

import lombok.Data;

@Data
public class ManticoreArticle {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章正文
     */
    private String content;

    /**
     * 文章图片
     */
    private String images;

    /**
     * 文章浏览数
     */
    private Integer views;

    /**
     * 文章评论数
     */
    private Integer comments;

    /**
     * 文章点赞数
     */
    private Integer likesCount;

    /**
     * 创建时间
     */
    private Long createdTime;

    /**
     * 更新时间
     */
    private Long updatedTime;

    /**
     * 状态 0:禁用，1:启用
     */
    private Integer status;
}
