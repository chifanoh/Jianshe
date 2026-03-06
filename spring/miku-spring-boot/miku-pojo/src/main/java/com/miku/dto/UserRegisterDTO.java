package com.miku.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {
    // 用户名
    @NotBlank(message = "用户名不能为空")
    @Size(message = "用户名长度不能超过6个字符", min = 1, max = 6)
    @Pattern(message = "用户名只能包含中文、字母、数字、下划线", regexp = "^[\\p{L}\\p{N}_]+$")
    private String name;

    // 用户邮箱
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[1-9]\\d{4,10}@qq\\.com$", message = "QQ邮箱格式不规范")
    private String email;

    // 用户密码
    @NotBlank(message = "密码不能为空")
    @Size(message = "密码长度6-10", min = 6 , max = 10)
    private String password;
}
