package com.what.spring.component.thirdAuth;

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

import java.util.Optional;

@Component
public class GithubThirdAuthConfig implements ThirdAuthConfig {

    private final GithubThirdAuthConfiguration configuration;

    private final WebClient webClient;

    @Resource(name = "myObjectMapper")
    private ObjectMapper objectMapper;

    private static final Logger LOG = LoggerFactory.getLogger(GithubThirdAuthConfig.class);


    GithubThirdAuthConfig(GithubThirdAuthConfiguration configuration, WebClient webClient) {
        this.configuration = configuration;
        this.webClient = webClient;
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
            String rawToken = webClient.get()
                    .uri(tokenUri)
                    .header("client_id", clientId)
                    .header("client_secret", clientSecret)
                    .header("code", thirdAuthCode)
                    .header("accept", "json")
                    .retrieve()
                    .bodyToMono(java.lang.String.class)
                    .block();
            Utils.NoEmptyOrBlank(rawToken, "get empty token");
            String token = rawToken.split("&")[0].split("=")[1];
            String rawGithubUserInfo = webClient.get()
                    .uri(userUri)
                    .header("Authorization", "token " + token)
                    .header("X-GitHub-Api-Version", apiVersion)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            Utils.NoEmptyOrBlank(rawGithubUserInfo, "get empty user info");
            GithubUserInfo githubUserInfo = objectMapper.readValue(rawGithubUserInfo, GithubUserInfo.class);
            return Optional.ofNullable(githubUserInfo);
        } catch (Exception e) {
            LOG.error("webClient error {}", e.getMessage());
            return Optional.empty();
        }
    }

}
