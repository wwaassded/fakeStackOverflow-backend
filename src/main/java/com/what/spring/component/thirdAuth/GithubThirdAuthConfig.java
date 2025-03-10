package com.what.spring.component.thirdAuth;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.pojo.thirAuth.EmailInfo;
import com.what.spring.pojo.thirAuth.GithubThirdAuthConfiguration;
import com.what.spring.pojo.thirAuth.GithubUserInfo;
import com.what.spring.pojo.thirAuth.ThirdPlatformUserInfo;
import com.what.spring.util.Utils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        String emailUri = Optional.ofNullable(configuration.getEmailUri()).orElse("").trim();
        Utils.NoEmptyOrBlank(clientId, "github client id is null or blank");
        Utils.NoEmptyOrBlank(clientSecret, "github thirdAuthCode is null or blank");
        //TODO 真正开始处理 请求逻辑
        try {
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
                    .header("X-GitHub-Api-Version", apiVersion)
                    .execute().body();
            Utils.NoEmptyOrBlank(rawGithubUserInfo, "get empty user info");
            GithubUserInfo githubUserInfo = objectMapper.readValue(rawGithubUserInfo, GithubUserInfo.class);
            if (githubUserInfo != null && (githubUserInfo.getEmail() == null || githubUserInfo.getEmail().isBlank())) {
                githubUserInfo.setEmail(getGithubUserEmial(emailUri, token));
            }
            return Optional.ofNullable(githubUserInfo);
        } catch (Exception e) {
            LOG.error("webClient error {}", e.getMessage());
            return Optional.empty();
        }
    }

    private String getGithubUserEmial(String uri, String token) throws JsonProcessingException {
        String rawGithubUserEmialInfo = HttpRequest.get(uri)
                .header("Authorization", "token " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .execute().body();
        List<EmailInfo> emailList = objectMapper.readValue(rawGithubUserEmialInfo, new TypeReference<List<EmailInfo>>() {
        });
        for (var email : emailList) {
            if (email.getPrimary()) {
                String res = email.getEmail();
                return res;
            }
        }
        return "";
    }

}
