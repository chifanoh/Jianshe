package com.miku.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserVO implements Serializable {

    //id
    private Long id;

    //用户头像
    private String avatar;

    //用户名
    private String name;

    //用户邮箱
    private String email;
}
