package com.what.spring.config;

import com.what.spring.pojo.user.UserSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class UtilConfig {
    @Bean(name = "sessionCache")
    public ConcurrentHashMap<String, UserSession> sessionCache() {
        //TODO 启动式可以考虑将多个用户session加载进来
        return new ConcurrentHashMap<>();
    }
}
