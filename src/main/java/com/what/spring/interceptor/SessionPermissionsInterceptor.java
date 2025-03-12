package com.what.spring.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.component.util.RedisExpirationListener;
import com.what.spring.pojo.user.SessionCounter;
import com.what.spring.pojo.user.UserSession;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("NullableProblems")
@Component("sessionPermissionsInterceptor")
@ConfigurationProperties(prefix = "redis.cache.user-session")
public class SessionPermissionsInterceptor implements HandlerInterceptor {

    @Value("${redis.cache.user-session.time}")
    private Integer time;

    @Value("${redis.cache.user-session.frequency}")
    private Integer frequency;

    @Resource(name = "myCacheThreadPool")
    private ThreadPoolExecutor cacheThreadPoolExecutor;

    @Resource(name = "redisExpirationListener")
    private RedisExpirationListener redisExpirationListener;

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "myObjectMapper")
    private ObjectMapper objectMapper;

    private static final Logger LOG = LoggerFactory.getLogger(SessionPermissionsInterceptor.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("拦截器开始工作");
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            LOG.info("拦截器失败");
            return false;
        }
        for (var cookie : cookies) {
            if (cookie.getName().equals("sessionId")) {
                String sessionId = cookie.getValue();
                ReentrantLock reentrantLock = redisExpirationListener.getReentrantLockCache().computeIfAbsent(sessionId, k -> new ReentrantLock());
                reentrantLock.lock();
                try {
                    ConcurrentHashMap<String, SessionCounter> sessionConcurrentHashMap = redisExpirationListener.getSessionCache();
                    if (sessionConcurrentHashMap.containsKey(sessionId)) {
                        SessionCounter sessionCounter = sessionConcurrentHashMap.get(sessionId);
                        UserSession userSession = sessionCounter.getUserSession();
                        sessionCounter.click();
                        request.setAttribute("userSession", userSession);
                        if (sessionCounter.getCount() >= this.frequency) {
                            cacheThreadPoolExecutor.execute(() -> {
                                redisTemplate.opsForValue().getAndExpire(sessionId, Duration.ofMinutes(this.time));
                            });
                        }
                    } else {
                        String userSessionJson = (String) redisTemplate.opsForValue().get(sessionId);
                        if (userSessionJson == null) {
                            LOG.info("拦截器失败");
                            return false;
                        }
                        UserSession userSession = objectMapper.readValue(userSessionJson, UserSession.class);
                        sessionConcurrentHashMap.put(sessionId, new SessionCounter(userSession, 1));
                        request.setAttribute("userSession", userSession);
                    }
                    LOG.info("拦截器成功喽");
                    return true;
                } finally {
                    reentrantLock.unlock();
                }
            }
        }
        LOG.info("拦截器失败");
        return false;
    }
}
