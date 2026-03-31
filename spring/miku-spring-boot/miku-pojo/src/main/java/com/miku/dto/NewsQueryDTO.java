package com.miku.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class NewsQueryDTO implements Serializable {

    // 获取几条新闻，默认5条
    private int pageSize = 5;

    // 分类
    private String category;
}