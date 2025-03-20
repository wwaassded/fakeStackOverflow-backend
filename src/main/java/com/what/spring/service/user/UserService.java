package com.what.spring.service.user;


import com.what.spring.mapper.UserMapper;
import com.what.spring.pojo.Result;
import com.what.spring.pojo.thirAuth.PlatfromUser;
import com.what.spring.pojo.user.NameAndPassword;
import com.what.spring.pojo.user.UserSession;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public Result getRawUserInfo(UserSession userSession) {
        PlatfromUser rawUserInfo = userMapper.getRawUserInfoByWebsiteId(userSession.getUserId());
        Result result = new Result();
        if (rawUserInfo != null) {
            result.fillSuccessFulResult(rawUserInfo, "success");
        } else {
            result.fillFailedResult(null, UserMapper.class, "无法找到用户信息");
        }
        return result;
    }

    @Transactional
    public void setUserAvatar(UserSession userSession, String avatar) {
        userMapper.setUserAvatar(userSession.getUserId(), avatar);
    }

    @Transactional
    public void createUserOnThirdPlatformUser(UserSession userSession, NameAndPassword nameAndPassword) {
    }
}
