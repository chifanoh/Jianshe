package com.miku.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {

    // 用户名或邮箱
    @NotBlank(message = "用户名不能为空")
    private String account;

    // 用户密码
    @NotBlank(message = "密码不能为空")
    private String password;

}
