package com.miku.service;

import com.miku.dto.NewsQueryDTO;
import com.miku.dto.NewsResultDTO;

public interface NewsService {

    /**
     * 获取新闻列表
     * @param queryDTO 查询参数
     * @return 新闻结果
     */
    NewsResultDTO getNews(NewsQueryDTO queryDTO);
}