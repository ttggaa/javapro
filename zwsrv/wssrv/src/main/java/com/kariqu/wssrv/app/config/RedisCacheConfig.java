package com.kariqu.wssrv.app.config;

import com.kariqu.zwsrv.thelib.cache.BaseRedisCacheConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by simon on 7/8/16.
 */
@Configuration
@ComponentScan
public class RedisCacheConfig extends BaseRedisCacheConfig {

    @Value(value = "${spring.redis.host}")
    private String host;

    @Value(value = "${spring.redis.port}")
    private int port;


    @Override
    public String theHost() {
        return host;
    }

    @Override
    public int thePort() {
        return port;
    }
}