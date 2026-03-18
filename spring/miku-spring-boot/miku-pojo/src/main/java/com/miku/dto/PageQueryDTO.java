package com.miku.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageQueryDTO implements Serializable {

    // 页码
    @Min(value = 1)
    private int page;

    // 每页显示记录数
    @Min(value = 1)
    @Max(value = 20)
    private int pageSize;
}
