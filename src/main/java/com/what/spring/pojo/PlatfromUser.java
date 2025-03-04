package com.what.spring.pojo;

import lombok.Data;

@Data
public class PlatfromUser {
    private int websiteId;

    private int thirdpartyId;

    private String userName;

    private String userEmail;

    private String avaterUrl;

    private String userPassword;

    private int is_active;

    private String createdAt;

    private int dataId;

    private int settingId;
}
