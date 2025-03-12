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
}
