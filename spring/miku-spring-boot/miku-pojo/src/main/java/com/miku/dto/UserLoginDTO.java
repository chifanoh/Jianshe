package com.miku.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {

    //用户名
    private String name;

    //用户邮箱
    private String email;

    //用户密码
    private String password;

}
