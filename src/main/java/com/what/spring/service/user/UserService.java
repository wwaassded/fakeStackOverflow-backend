package com.what.spring.service.user;


import com.what.spring.mapper.UserMapper;
import com.what.spring.pojo.Result;
import com.what.spring.pojo.thirAuth.PlatfromUser;
import com.what.spring.pojo.user.UserSession;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;


import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public Result getRawUserInfo(Integer websiteId, Future<Optional<UserSession>> future) throws ExecutionException, InterruptedException {
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
