package com.miku.dto;

import lombok.Data;

@Data
public class SearchDTO {

    // 搜索关键字参数
    private String query;

    // 页码
    private int page;

    // 每页显示记录数
    private int pageSize;
}
