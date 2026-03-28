package com.miku.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 用户表
* @TableName user
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    /**
    * id
    */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户头像
     */
    private String avatar;

    /**
    * 性别
    */
    private String sex;

    /**
     * 关注
     */
    private Integer follow;

    /**
     * 粉丝
     */
    private Integer fans;

    /**
     * 收藏
     */
    private Integer favorites;

    /**
    * 手机号
    */
    private String phone;

    /**
    * 用户密码
    */
    private String password;

    /**
    * 用户邮箱
    */
    private String email;

    /**
     * 启用禁用
     */
    private int status;

    /**
    * 创建日期
    */
    private LocalDateTime createdTime;

    /**
    * 修改日期
    */
    private LocalDateTime updatedTime;
}
