package com.what.spring.component;

import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.pojo.GithubThirdAuthConfiguration;
import com.what.spring.pojo.ThirdPlatformUserInfo;
import com.what.spring.util.Utils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GithubThirdAuthConfig implements ThirdAuthConfig {

    private final GithubThirdAuthConfiguration configuration;

    GithubThirdAuthConfig(GithubThirdAuthConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Optional<ThirdPlatformUserInfo> getThirdPlatformUserInfo(String thirdAuthCode) throws StringEmptyOrNull {
        Utils.NoEmptyOrBlank(thirdAuthCode, "github thirdAuthCode is null or blank");
        String clientId = Optional.ofNullable(configuration.getClientid()).orElse("").trim();
        String clientSecret = Optional.ofNullable(configuration.getClientid()).orElse("").trim();
        Utils.NoEmptyOrBlank(clientId, "github client id is null or blank");
        Utils.NoEmptyOrBlank(clientSecret, "github thirdAuthCode is null or blank");
        //TODO 真正开始处理 请求逻辑
    }

}
