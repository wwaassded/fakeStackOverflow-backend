package com.what.spring.component.thirdAuth;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.pojo.thirAuth.GithubThirdAuthConfiguration;
import com.what.spring.pojo.thirAuth.GithubUserInfo;
import com.what.spring.pojo.thirAuth.ThirdPlatformUserInfo;
import com.what.spring.util.Utils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Component
public class GithubThirdAuthConfig implements ThirdAuthConfig {

    private final GithubThirdAuthConfiguration configuration;


    @Resource(name = "myObjectMapper")
    private ObjectMapper objectMapper;

    private static final Logger LOG = LoggerFactory.getLogger(GithubThirdAuthConfig.class);


    GithubThirdAuthConfig(GithubThirdAuthConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Optional<ThirdPlatformUserInfo> getThirdPlatformUserInfo(String thirdAuthCode) throws StringEmptyOrNull {
        Utils.NoEmptyOrBlank(thirdAuthCode, "github thirdAuthCode is null or blank");
        String clientId = Optional.ofNullable(configuration.getClientId()).orElse("").trim();
        String clientSecret = Optional.ofNullable(configuration.getClientSecret()).orElse("").trim();
        String tokenUri = Optional.ofNullable(configuration.getTokenUri()).orElse("").trim();
        String userUri = Optional.ofNullable(configuration.getUserUri()).orElse("").trim();
        String apiVersion = Optional.ofNullable(configuration.getApiVersion()).orElse("").trim();
        Utils.NoEmptyOrBlank(clientId, "github client id is null or blank");
        Utils.NoEmptyOrBlank(clientSecret, "github thirdAuthCode is null or blank");
        //TODO 真正开始处理 请求逻辑
        try {
            LOG.info("i am here");
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("client_id", clientId);
            paramMap.put("client_secret", clientSecret);
            paramMap.put("code", thirdAuthCode);
            paramMap.put("accept", "json");
            String rawToken = HttpUtil.post(tokenUri, paramMap);
            Utils.NoEmptyOrBlank(rawToken, "get empty token");
            String token = rawToken.split("&")[0].split("=")[1];
            String rawGithubUserInfo = HttpRequest.get(userUri)
                    .header("Authorization", "token " + token)
                    .header("X-GitHub-Api-Version", "2022-11-28")
                    .execute().body();
            Utils.NoEmptyOrBlank(rawGithubUserInfo, "get empty user info");
            GithubUserInfo githubUserInfo = objectMapper.readValue(rawGithubUserInfo, GithubUserInfo.class);
            return Optional.ofNullable(githubUserInfo);
        } catch (Exception e) {
            LOG.error("webClient error {}", e.getMessage());
            return Optional.empty();
        }
    }

}
