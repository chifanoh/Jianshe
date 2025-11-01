package com.miku.config;

import com.miku.interceptor.JwtTokenUserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final JwtTokenUserInterceptor jwtTokenUserInterceptor;

    //final常量拦截字段
    public final class SkipJwtUrls{
        public static final String[] LIST = {
                "/api/article/create",
                "/api/user/current",
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns(SkipJwtUrls.LIST)  // 拦截
                .excludePathPatterns();             // 放行
    }
}
