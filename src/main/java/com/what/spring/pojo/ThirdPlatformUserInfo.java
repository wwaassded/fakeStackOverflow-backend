package com.what.spring.pojo;

import lombok.Data;

@Data
public class ThirdPlatformUserInfo {
    ThirdPlatformUserInfo(Long userId, String avatarURL, String email, String htmlURL) {
        this.userId = userId;
        this.avatarURL = avatarURL;
        //TODO 这里的邮箱初始化应该需要校验一下
        this.email = email;
        this.htmlURL = htmlURL;
    }

    private Long userId;
    private String avatarURL;
    private String email;
    private String htmlURL;
}
