package com.miku.interceptor;

import com.miku.constant.JwtClaimsConstant;
import com.miku.context.BaseContext;
import com.miku.properties.JwtProperties;
import com.miku.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("=========JwtInterceptor 进入=========");
        // 判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod hm)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //如果类或方法打了 @SkipJwt，直接放行        (需要再打开)
//        if (hm.hasMethodAnnotation(SkipJwt.class) || hm.getBeanType().isAnnotationPresent(SkipJwt.class)) {
//            return true;
//        }

        // 只读 Cookie，没有就 401
        Cookie cookie = WebUtils.getCookie(request, jwtProperties.getCookieName());
        if (cookie == null || cookie.getValue() == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        String token = cookie.getValue();

        // 校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            String userId = claims.get(JwtClaimsConstant.USER_ID).toString();
            log.info("当前用户的id:{}", userId);
            BaseContext.setCurrentId(userId);

            // redis黑名单校验
            String jti = claims.getId();
            if (Boolean.TRUE.equals(redisTemplate.hasKey("delay:" + jti))) {
                log.info("redis黑名单存在该用户ID为:{}的token", BaseContext.getCurrentId());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            // 通过，放行
            return true;
        } catch (Exception ex) {
            // 不通过，响应401状态码
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
