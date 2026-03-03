package com.miku.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginDTO implements Serializable {

    //用户名
    @NotBlank(message = "用户名不能为空")
    @Size(message = "用户名最多不能超过{max}个字符", max = 6)
    @Pattern(message = "用户名只能包含字母、数字、下划线", regexp = "^[a-zA-Z0-9_]+$")
    private String name;

    //用户邮箱
    private String email;

    //用户密码
    @NotBlank(message = "密码不能为空")
    private String password;

}
