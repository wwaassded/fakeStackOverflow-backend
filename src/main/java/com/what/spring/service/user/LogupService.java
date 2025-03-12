package com.what.spring.service.user;

import com.what.spring.pojo.user.NameAndPassword;
import com.what.spring.util.Utils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class LogupService {

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "myCacheThreadPool")
    private ThreadPoolExecutor cacheThreadPoolExecutor;

    public String cacheNameAndPassword(NameAndPassword nameAndPassword) {
        String key = Utils.getNAPKey(nameAndPassword);
        cacheThreadPoolExecutor.execute(() -> redisTemplate.opsForValue().set(key, nameAndPassword, Duration.ofMinutes(1)));
        return key;
    }

    public void emailHandler(String email, String key) {

    }
}
