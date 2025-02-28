package com.what.spring.service;

import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.component.ThirdAuthConfig;
import com.what.spring.pojo.Result;
import com.what.spring.pojo.ThirdPlatformUserInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
public class GithubThirdAuthService extends ThirdAuthService {

    @Autowired
    GithubThirdAuthService(WebClient webClient) {
        super(webClient);
    }

    @Resource(name = "githubThirdAuthConfig")
    private ThirdAuthConfig thirdAuthConfig;

    @Override
    public Result thirdAuthHandle(String thirdAuthCode) throws StringEmptyOrNull {
        Optional<ThirdPlatformUserInfo> thirdPlatformUserInfo = thirdAuthConfig.getThirdPlatformUserInfo(thirdAuthCode);
    }
}
