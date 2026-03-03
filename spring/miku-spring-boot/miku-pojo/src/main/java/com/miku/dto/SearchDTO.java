package com.miku.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SearchDTO {

    // 搜索关键字参数
    @NotBlank(message = "关键字不能为空")
    @Size(message = "搜索内容最长不能超过{20}个字符", max = 20)
    private String query;

    // 页码
    @Min(value = 1)
    private int page;

    // 每页显示记录数
    @Min(value = 1)
    @Max(value = 20)
    private int pageSize;
}
