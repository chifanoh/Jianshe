package com.miku.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miku.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 用户登录
     * @param name
     * @return
     */
    User selectByUserName(String name);
    User selectByEmail(String account);

    /**
     * 用户注册
     * @param name
     * @return
     */
    String countByName(String name);
    String countByEmail(String email);


}
