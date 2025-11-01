package com.miku.service;

import com.miku.dto.UserLoginDTO;
import com.miku.entity.User;

public interface UserService {

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     * Cookie:密钥验签
     * @param uid
     * @return
     */
    User current(Long uid);
}
