package com.what.spring.service.user;

import com.what.spring.mapper.UserMapper;
import com.what.spring.pojo.Result;
import com.what.spring.pojo.thirAuth.PlatfromUser;
import com.what.spring.pojo.user.UserSession;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class UserService {

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserMapper userMapper;

    public Optional<UserSession> getWebSiteIdFromSession(HttpServletRequest httpServletRequest) {
        for (var cookie : httpServletRequest.getCookies()) {
            if (cookie.getName().equals("sessionId")) {
                String sessionId = cookie.getValue();
                Optional<UserSession> optionalUserSession = Optional.ofNullable((UserSession) redisTemplate.opsForValue().get(sessionId));
                // 活跃用户刷新缓存的时间
                optionalUserSession.ifPresent(userSession -> redisTemplate.opsForValue().set(sessionId, userSession, Duration.ofHours(1)));
                return optionalUserSession;
            }
        }
        return Optional.empty();
    }

    public Result getRawUserInfo(Integer websiteId, HttpServletRequest httpServletReques, Future<Optional<UserSession>> future) throws ExecutionException, InterruptedException {
        PlatfromUser rawUserInfo = userMapper.getRawUserInfoByWebsiteId(websiteId);
        if (rawUserInfo != null) {
            Optional<UserSession> optionalUserSession = future.get();
        } else {
            Result result = new Result();
            result.fillFailedResult(null, UserMapper.class, "无法找到用户信息");
            return result;
        }
    }
}
