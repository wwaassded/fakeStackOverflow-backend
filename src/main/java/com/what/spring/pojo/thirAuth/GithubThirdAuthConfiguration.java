package com.what.spring.pojo.thirAuth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "thirdauthconfig.github")
public class GithubThirdAuthConfiguration {

    private String clientId;
    private String clientSecret;
    private String tokenUri;
    private String userUri;
    private String emailUri;
    private String apiVersion;
}
