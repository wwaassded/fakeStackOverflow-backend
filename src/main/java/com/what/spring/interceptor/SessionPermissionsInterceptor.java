package com.what.spring.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.pojo.user.UserSession;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("NullableProblems")
@Component("sessionPermissionsInterceptor")
public class SessionPermissionsInterceptor implements HandlerInterceptor {

    @Resource(name = "sessionCache")
    private ConcurrentHashMap<String, UserSession> sessionHashMap;

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
                if (sessionHashMap.containsKey(sessionId)) {
                    request.setAttribute("userSession", sessionHashMap.get(sessionId));
                } else {
                    String userSessionJson = (String) redisTemplate.opsForValue().get(sessionId);
                    if (userSessionJson == null) {
                        LOG.info("拦截器失败");
                        return false;
                    }
                    UserSession userSession = objectMapper.readValue(userSessionJson, UserSession.class);
                    sessionHashMap.put(sessionId, userSession);
                    request.setAttribute("userSession", userSession);
                }
                LOG.info("拦截器成功喽");
                return true;
            }
        }
        LOG.info("拦截器失败");
        return false;
    }
}
