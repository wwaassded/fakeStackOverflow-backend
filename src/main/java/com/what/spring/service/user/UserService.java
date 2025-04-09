package com.what.spring.service.user;


import com.what.spring.Exception.CannotFindUser;
import com.what.spring.Exception.ExceptionEnum;
import com.what.spring.mapper.ThirdPlatFormInfoMapper;
import com.what.spring.mapper.UserMapper;
import com.what.spring.pojo.Result;
import com.what.spring.pojo.thirAuth.PlatfromUser;
import com.what.spring.pojo.user.NameAndPassword;
import com.what.spring.pojo.user.ThirdPlatFromInfo;
import com.what.spring.pojo.user.UserSession;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ThirdPlatFormInfoMapper thirdPlatFormInfoMapper;

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
    public void createUserOnThirdPlatformUser(UserSession userSession, NameAndPassword nameAndPassword) throws CannotFindUser {
        //TODO 填充用户表中的所有外键 现阶段只实现填充第三方账号的信息
        int websiteId = userSession.getUserId();
        PlatfromUser platfromUser = userMapper.getRawUserInfoByWebsiteId(websiteId);
        if (platfromUser == null) {
            throw new CannotFindUser(ExceptionEnum.USER_NOT_DOUND);
        }
        if (nameAndPassword.getName() == null) {
            nameAndPassword.setName(platfromUser.getUserName());
        }
        if (nameAndPassword.getEmail() == null) {
            nameAndPassword.setEmail(platfromUser.getUserEmail());
        }
        userMapper.updateThridPlatformUser(websiteId, nameAndPassword);
        ThirdPlatFromInfo thirdPlatFromInfo = new ThirdPlatFromInfo(userSession, platfromUser);
        thirdPlatFormInfoMapper.InsertIntoThirdPlatFormInfoRow(thirdPlatFromInfo);
    }
}
