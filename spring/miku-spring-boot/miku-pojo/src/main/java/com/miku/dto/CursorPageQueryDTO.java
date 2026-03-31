package com.miku.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;

@Data
public class CursorPageQueryDTO implements Serializable {
    // 每页显示记录数
    @Min(value = 1)
    @Max(value = 20)
    private int pageSize;

    // 游标
    private Long cursor;

    // 排序方向
    private String direction = "desc";
}