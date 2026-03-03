package com.miku.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    /** MySQL - @Primary 给 MyBatis 用 */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public HikariDataSource mysqlDataSource() {
        return new HikariDataSource();
    }

    /** Manticore - 只用于搜索 */
    @Bean(name = "manticoreDataSource")
    public DataSource manticoreDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:9306/");
        config.setUsername("");
        config.setPassword("");
        config.setMaximumPoolSize(3);
        return new HikariDataSource(config);
    }
}
