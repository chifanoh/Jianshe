package com.miku.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserArticleDetailVO {
    //id
    private Long id;

    //用户名
    private String name;

    //用户头像
    private String avatar;

    //关注
    private Integer follow;

    //粉丝
    private Integer fans;
}
