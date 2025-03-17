package com.what.spring.pojo.thirAuth;

import com.what.spring.pojo.user.NameAndPassword;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlatfromUser {
    private int websiteId;

    private int thirdpartyId;

    private String userName;

    private String userEmail;

    private String avatarUrl;

    private String userPassword;

    private int is_active;

    private String createdAt;

    private int dataId;

    private int settingId;

    public void initByNameAndPassword(NameAndPassword nap, String avatarUrl) {
        userName = nap.getName();
        userPassword = nap.getPassword();
        userEmail = nap.getEmail();
        is_active = 1;
        this.avatarUrl = avatarUrl;
        createdAt = LocalDateTime.now().toString();
    }
}
