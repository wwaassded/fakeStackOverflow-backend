package com.what.spring.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

@Getter
@Component
@ConfigurationProperties(prefix = "nginx")
public class NginxProperties {

    @Value("${nginx.root}")
    private String nginxRoot;

    @Value("${nginx.avatar.redis-key}")
    private String avatarRedisKey;

    @Value("${nginx.avatar.root}")
    private String nginxAvatarRoot;

    @Value("${nginx.avatar.defaultAvatar}")
    private String defaultAvatarPng;

    @Value("${nginx.localdir}")
    private String localDir;

    private final File localFileDir;

    public String getDefaultAvatarUrl() {
        return nginxRoot + nginxAvatarRoot + defaultAvatarPng;
    }

    NginxProperties() {
        localFileDir = new File(localDir);
    }
}
