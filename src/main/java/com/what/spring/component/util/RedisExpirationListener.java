package com.what.spring.component.util;


import com.what.spring.pojo.user.SessionCounter;
import lombok.Getter;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Component("redisExpirationListener")
public class RedisExpirationListener implements MessageListener {

    private final ConcurrentHashMap<String, SessionCounter> sessionCache = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, ReentrantLock> reentrantLockCache = new ConcurrentHashMap<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith("SessionELF")) {
            System.out.println("Session key expired: " + expiredKey);
            sessionCache.remove(expiredKey);
        }
    }
}
