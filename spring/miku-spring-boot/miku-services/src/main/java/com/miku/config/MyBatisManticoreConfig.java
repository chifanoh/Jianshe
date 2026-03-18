package com.miku.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.miku.mapper.manticore", sqlSessionFactoryRef = "manticoreSqlSessionFactory")
public class MyBatisManticoreConfig {

    @Bean("manticoreSqlSessionFactory")
    public SqlSessionFactory manticoreSqlSessionFactory(
            @Qualifier("manticoreDataSource") DataSource dataSource) throws Exception {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        // 设置Manticore的XML映射文件路径
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/manticore/**/*.xml")
        );
        return factoryBean.getObject();
    }
}