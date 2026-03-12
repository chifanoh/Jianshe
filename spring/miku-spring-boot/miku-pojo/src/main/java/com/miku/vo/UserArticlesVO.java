package com.miku.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserArticlesVO implements Serializable {
    //id
    private String id;

    //用户名
    private String name;

    //用户头像
    private String avatar;
}
