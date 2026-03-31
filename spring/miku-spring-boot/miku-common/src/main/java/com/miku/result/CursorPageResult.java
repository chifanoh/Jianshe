package com.miku.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CursorPageResult<T> implements Serializable {

    private Long total; //总记录数

    private Long cursor; // 游标

    private List<T> records; //当前页数据集合
}
