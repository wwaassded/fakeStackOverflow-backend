package com.what.spring.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.pojo.thirAuth.PlatfromUser;
import com.what.spring.pojo.user.UserSession;
import com.what.spring.util.Utils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class SessionService {
    @Resource(name = "myObjectMapper")
    private ObjectMapper objectMapper;

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "myCacheThreadPool")
    private ThreadPoolExecutor cacheThreadPool;

    public UserSession userSessionHandle(PlatfromUser platfromUser, HttpServletResponse response) throws JsonProcessingException {
        UserSession userSession = new UserSession(false);
        userSession.fillUserInfo(platfromUser);
        String sessionId = Utils.getSessionId(userSession);
        userSession.setSessionId(sessionId);
        String jsonSession = objectMapper.writeValueAsString(userSession);
        Cookie sessionCookie = Utils.getGlobalCookie("sessionId", sessionId, 3600);
        response.addCookie(sessionCookie);
        cacheThreadPool.execute(() -> redisTemplate.opsForValue().set(sessionId, jsonSession, Duration.ofHours(1)));
        return userSession;
    }
}
