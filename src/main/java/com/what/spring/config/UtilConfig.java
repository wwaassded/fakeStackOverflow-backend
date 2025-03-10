package com.what.spring.config;

import com.what.spring.pojo.user.SessionCounter;
import com.what.spring.pojo.user.UserSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
public class UtilConfig {
    @Bean(name = "sessionCache")
    public ConcurrentHashMap<String, SessionCounter> sessionCache() {
        //TODO 启动式可以考虑将多个用户session加载进来
        return new ConcurrentHashMap<>();
    }

    @Bean(name = "ReentrantLockCache")
    public ConcurrentHashMap<String, ReentrantLock> reentrantLockCache() {
        //TODO 启动式可以考虑将多个用户session加载进来
        return new ConcurrentHashMap<>();
    }
}
