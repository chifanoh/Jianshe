package com.miku.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class NewsItem implements Serializable {

    // 新闻标题
    private String title;

    // 新闻描述
    private String description;

    // 新闻链接
    private String url;

    // 图片链接
    private String imageUrl;

    // 发布时间
    private String publishedAt;

    // 来源
    private String source;
}