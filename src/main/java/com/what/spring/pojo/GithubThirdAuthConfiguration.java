package com.what.spring.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "thirdauthconfig.github")
public class GithubThirdAuthConfiguration {

    private String clientid;
    private String clientsecret;
}
