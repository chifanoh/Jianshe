package com.miku;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling  // 开启定时任务
@SpringBootApplication
public class MikuServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MikuServicesApplication.class, args);
        log.info("========== server started ==========");
    }

}
