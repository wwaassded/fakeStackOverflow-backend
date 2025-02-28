package com.what.spring.service;

import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.component.ThirdAuthConfig;
import com.what.spring.pojo.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class GithubThirdAuthService implements ThirdAuthService {

    @Resource(name = "githubThirdAuthConfig")
    private ThirdAuthConfig thirdAuthConfig;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Result thirdAuthHandle(String thirdAuthCode) throws StringEmptyOrNull {
        var thirdPlatformUserInfo = thirdAuthConfig.getThirdPlatformUserInfo(thirdAuthCode);
        Result result = new Result();
        thirdPlatformUserInfo.ifPresentOrElse(userInfo -> {
            //处理用户 包括创建用户 绑定用户等
        }, () -> {
            //向result中设置失败信息
            result.setIsSuccessful(false);
        });
        return result;
    }
}
