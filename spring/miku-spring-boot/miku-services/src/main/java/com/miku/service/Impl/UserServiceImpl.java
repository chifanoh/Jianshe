package com.miku.service.Impl;

import com.miku.constant.MessageConstant;
import com.miku.dto.UserLoginDTO;
import com.miku.dto.UserRegisterDTO;
import com.miku.entity.User;
import com.miku.exception.AccountNotFoundException;
import com.miku.exception.PasswordErrorException;
import com.miku.mapper.mysql.UserMapper;
import com.miku.service.UserService;
import com.miku.utils.LoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * Cooike:密钥验签
     *
     * @param uid
     * @return
     */
    @Override
    public User current(Long uid) {
        User user = userMapper.selectById(uid);
        return user;
    }

    /**
     * 用户注册
     *
     * @param userRegisterDTO
     */
    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        // 查数据库是否重复
        if (userMapper.countByName(userRegisterDTO.getName()) != null) {
            throw new PasswordErrorException(MessageConstant.USERNAME_ALREADY_EXISTS);
        }
        if (userMapper.countByEmail(userRegisterDTO.getEmail()) != null) {
            throw new PasswordErrorException(MessageConstant.EMAIL_ALREADY_EXISTS);
        }

        User user = new User();
        // md5加密
        String md5 = DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes(StandardCharsets.UTF_8));
        // 获取qq头像
        String url = "http://q1.qlogo.cn/g?b=qq&nk=" + userRegisterDTO.getEmail() + "&s=100";
        // 拼接数据
        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(md5);
        user.setAvatar(url);
        user.setCreatedTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        User user;
        String password = userLoginDTO.getPassword();
        if (LoginUtil.isEmail(userLoginDTO.getAccount())) {
            user = userMapper.selectByEmail(userLoginDTO.getAccount());
        } else {
            user = userMapper.selectByUserName(userLoginDTO.getAccount());
        }

        // 处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (user == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // MD5加密
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        // 密码比对
        if (!md5.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        // 返回实体对象
        return user;
    }
}
