package com.miku.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 主题贴表
* @TableName articles
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Articles implements Serializable {

    /**
    * 主键ID
    */
    @TableId(type = IdType.AUTO)
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
     * 正文摘要
     */
    private String summary;

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
    private LocalDateTime createdTime;

    /**
    * 更新时间
    */
    private LocalDateTime updatedTime;

    /**
    * 状态 0:禁用，1:启用
    */
    private Integer status;
}