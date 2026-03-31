package com.miku.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NewsResultDTO implements Serializable {

    // 新闻列表
    private List<com.miku.dto.NewsItem> records;

    // 缓存时间戳
    private long cacheTime;

    // 是否来自缓存
    private boolean fromCache;
}