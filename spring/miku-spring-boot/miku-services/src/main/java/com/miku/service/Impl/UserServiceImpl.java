package com.miku.service.Impl;

import com.miku.constant.MessageConstant;
import com.miku.dto.UserLoginDTO;
import com.miku.entity.User;
import com.miku.exception.AccountNotFoundException;
import com.miku.exception.PasswordErrorException;
import com.miku.mapper.mysql.UserMapper;
import com.miku.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     *用户登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String name = userLoginDTO.getName();
        String password = userLoginDTO.getPassword();

//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUserName,userName);
//        User user = (User) getOne(wrapper);

        User user = userMapper.selectByUserName(name);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (user == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //密码比对
        try {
            if (!password.equals(user.getPassword())) {
                //密码错误
                throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
            }
        }catch (PasswordErrorException e){
            return null;
        }
        //3、返回实体对象
        return user;
    }

    /**
     * Cooike:密钥验签
     * @param uid
     * @return
     */
    @Override
    public User current(Long uid) {
        User user = userMapper.selectById(uid);
        return user;
    }
}
