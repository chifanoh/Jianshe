package com.miku.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    /** MySQL - @Primary 给 MyBatis 用 */
    @Primary
    @Bean("mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    /** Manticore - 只用于搜索 */
    @Bean("manticoreDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.manticore")
    public DataSource manticoreDataSource() {
        return DataSourceBuilder.create().build();
    }
}
