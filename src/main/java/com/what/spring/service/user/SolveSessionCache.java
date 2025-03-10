package com.what.spring.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.pojo.user.UserSession;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class SolveSessionCache {

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "myObjectMapper")
    private ObjectMapper objectMapper;

    public Optional<UserSession> getWebSiteIdFromSession(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        if (httpServletRequest.getCookies() == null) {
            return Optional.empty();
        }
        for (var cookie : httpServletRequest.getCookies()) {
            if (cookie.getName().equals("sessionId")) {
                String sessionId = cookie.getValue();
                String sessionJsonStr = (String) redisTemplate.opsForValue().get(sessionId);
                if (sessionJsonStr == null) {
                    return Optional.empty();
                }
                UserSession session = objectMapper.readValue(sessionJsonStr, UserSession.class);
                // 活跃用户刷新缓存的时间
                if (session != null) {
                    redisTemplate.opsForValue().set(sessionId, sessionJsonStr, Duration.ofHours(1));
                }
                return Optional.ofNullable(session);
            }
        }
        return Optional.empty();
    }

}
