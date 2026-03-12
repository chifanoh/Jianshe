package com.miku.service;

import com.miku.dto.UserLoginDTO;
import com.miku.dto.UserRegisterDTO;
import com.miku.entity.User;

public interface UserService {
    /**
     * Cookie:密钥验签
     * @param uid
     * @return
     */
    User current(String uid);

    /**
     * 用户注册
     * @param userRegisterDTO
     */
    void register(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);
}
