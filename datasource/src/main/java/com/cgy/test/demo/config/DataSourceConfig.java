package com.cgy.test.demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "mytestDataSource")
    @Qualifier("mytestDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mytest")
    @Primary
    public DataSource mytestDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mywebDataSource")
    @Qualifier("mywebDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.myweb")
    public DataSource mywebDataSource() {
        return DataSourceBuilder.create().build();
    }
}
