package com.what.spring.service;

import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.component.ThirdAuthConfig;
import com.what.spring.mapper.UserMapper;
import com.what.spring.pojo.GithubUserInfo;
import com.what.spring.pojo.PlatfromUser;
import com.what.spring.pojo.Result;
import jakarta.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class GithubThirdAuthService implements ThirdAuthService {

    @Resource(name = "githubThirdAuthConfig")
    private ThirdAuthConfig thirdAuthConfig;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Result thirdAuthHandle(String thirdAuthCode) throws StringEmptyOrNull, DataAccessException {
        var thirdPlatformUserInfo = thirdAuthConfig.getThirdPlatformUserInfo(thirdAuthCode);
        Result result = new Result();
        thirdPlatformUserInfo.ifPresentOrElse(userInfo -> {
            //处理用户 包括创建用户 绑定用户等
            if (userInfo instanceof GithubUserInfo user) { // 第三方信息返回的应该是github的用户信息
                int githubUserId = user.getUserId();
                Optional<PlatfromUser> platfromUser = Optional.ofNullable(userMapper.getPlatformUserByThirdplatformId(githubUserId));
                platfromUser.ifPresentOrElse(realUser -> {
                    result.setIsSuccessful(true);
                    result.setObject(realUser);
                    result.setMessage("已经关联账号了直接登录");
                }, () -> {
                    //如果用户并不存在
                    //以github用户信息为中心创建用户
                    PlatfromUser newPlatfromUser = createActiveGithubBasedUser(user);
                    userMapper.insertPlatformUserWithDefaultTime(newPlatfromUser);
                    //TODO: 后续可能会添加创建用户相关联的第三方平台信息
                    result.setObject(newPlatfromUser);
                    result.setIsSuccessful(true);
                    result.setMessage("创建了基于github第三方用户的账号");
                });
            }
        }, () -> {
            result.setIsSuccessful(false);
            result.setMessage("thirdAuthConfig.getThirdPlatformUserInfo return null value");
            result.setTroubleMaker(thirdAuthCode.getClass().getName());
        });
        return result;
    }

    private static PlatfromUser createActiveGithubBasedUser(GithubUserInfo userInfo) {
        PlatfromUser user = new PlatfromUser();
        user.setUserName(userInfo.getUserName());
        user.setThirdpartyId(userInfo.getUserId());
        user.setUserEmail(userInfo.getEmail());
        user.setAvaterUrl(userInfo.getAvatarURL());
        user.setIs_active(1);
        return user;
    }
}
