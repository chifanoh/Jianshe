package com.miku.controller.user;

import com.miku.constant.JwtClaimsConstant;
import com.miku.context.BaseContext;
import com.miku.dto.UserLoginDTO;
import com.miku.entity.User;
import com.miku.properties.JwtProperties;
import com.miku.result.Result;
import com.miku.service.UserService;
import com.miku.utils.JwtUtil;
import com.miku.vo.UserLoginVO;
import com.miku.vo.UserVO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
@Slf4j
@CrossOrigin(origins = {"http://localhost:5173"},allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Cookie:密钥验签 + 返回用户数据 ——> IndexDB
     * @param request
     * @return
     */
    @GetMapping("/current")
    public Result<UserVO> current(HttpServletRequest request){
        log.info("密钥验签:{}",request);

        Long uid = BaseContext.getCurrentId();
        if (uid == null){
            return Result.error("未登录");
        }

        //查库
        User user = userService.current(uid);
        //验签通过返回给前端
        UserVO userVO = UserVO.builder()
                .id(user.getId())
                .avatar(user.getAvatar())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return Result.success(userVO);
    }

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response){
        log.info("用户登录:{}", userLoginDTO);
        User user = userService.login(userLoginDTO);

        if (user==null){
            return Result.error("密码错误");
        }

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        //使用HttpOnly Cookie
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);        // 禁止 JS 读取
        cookie.setSecure(false);         // HTTPS 场景下开启
        cookie.setAttribute("SameSite","Strict");    // 防 CSRF
        cookie.setPath("/");             // 整站通用
        cookie.setMaxAge((int) (jwtProperties.getUserTtl() / 1000)); // 秒
        response.addCookie(cookie);


        //返回给前端的数据
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .avatar(user.getAvatar())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }

    /**
     * 退出登录
     * @param request
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = WebUtils.getCookie(request, jwtProperties.getCookieName());

        // cookie为空
        if (cookie == null) {
            return Result.error("未登录");
        }

        String jwt = cookie.getValue();
        log.info("待登出token:{}", jwt);

        // jwt过期时间
        Claims exp = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), jwt);
        // 计算ttl存redis
        long ttl = exp.getExpiration().getTime();
        redisTemplate.opsForValue().set("delay:" + BaseContext.getCurrentId(), jwt , ttl, TimeUnit.SECONDS);
        return Result.success();
    }
}
