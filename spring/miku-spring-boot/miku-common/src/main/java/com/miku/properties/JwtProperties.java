package com.miku.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "miku.jwt")
@Data
public class JwtProperties {

    /**
     * 管理端员工生成jwt令牌相关配置
     */
    private long adminTtl;
    private String adminSecretKey;
    private String adminTokenName;

    /**
     * 用户端用户生成jwt令牌相关配置
     */
    private long userTtl;
    private String userSecretKey;
    private String userTokenName;
    private String cookieName = "access_token";
}
