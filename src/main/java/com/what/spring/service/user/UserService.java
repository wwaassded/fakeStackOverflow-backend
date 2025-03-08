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
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class UserService {

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserMapper userMapper;
    private ThreadPoolExecutor myCacheThreadPool;

    public Optional<UserSession> getWebSiteIdFromSession(HttpServletRequest httpServletRequest) {
        for (var cookie : httpServletRequest.getCookies()) {
            if (cookie.getName().equals("sessionId")) {
                String sessionId = cookie.getValue();
                Optional<UserSession> optionalUserSession = Optional.ofNullable((UserSession) redisTemplate.opsForValue().get(sessionId));
                // 活跃用户刷新缓存的时间
                optionalUserSession.ifPresent(userSession -> {
                    userSession.setLastAccessedTime(LocalDateTime.now());
                    redisTemplate.opsForValue().set(sessionId, userSession, Duration.ofHours(1));
                });
                return optionalUserSession;
            }
        }
        return Optional.empty();
    }

    public Result getRawUserInfo(Integer websiteId, HttpServletRequest httpServletReques, Future<Optional<UserSession>> future) throws ExecutionException, InterruptedException {
        //Fixme 这里的数据库访问可能是不必要的 session 中存储的数据足够使用
        PlatfromUser rawUserInfo = userMapper.getRawUserInfoByWebsiteId(websiteId);
        if (rawUserInfo != null) {
            Optional<UserSession> optionalUserSession = future.get();
            Result result = new Result();
            if (optionalUserSession.isPresent()) {
                Integer sessionWebSiteId = optionalUserSession.get().getUserId();
                if (Objects.equals(sessionWebSiteId, rawUserInfo.getWebsiteId()) || optionalUserSession.get().getIsAdmin()) {
                    result.fillSuccessFulResult(rawUserInfo, "success");
                } else {
                    result.fillFailedResult(null, optionalUserSession.getClass(), "身份验证失败");
                }
            } else {
                result.fillFailedResult(null, this.getClass(), "getWebSiteIdFromSession:缓存任务执行失败");
            }
            return result;
        } else {
            Result result = new Result();
            result.fillFailedResult(null, UserMapper.class, "无法找到用户信息");
            return result;
        }
    }
}
