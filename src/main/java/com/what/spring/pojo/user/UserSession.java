package com.what.spring.pojo.user;

import com.what.spring.pojo.thirAuth.PlatfromUser;
import lombok.Data;

import java.time.LocalDateTime;

import static cn.hutool.core.util.StrUtil.uuid;

@Data
public class UserSession {
    public UserSession(Boolean isAdmin) {
        this();
        this.isAdmin = isAdmin;
    }

    public void fillUserInfo(PlatfromUser user) {
        this.userId = user.getWebsiteId();
        this.avaterUrl = user.getAvatarUrl();
        this.userEmail = user.getUserEmail();
        this.userName = user.getUserName();
        this.thirdPlatformUserId = user.getThirdpartyId();
    }

    public UserSession() {
        this.isAdmin = false;
        this.needDelete = false;
        this.sessionCreationTime = LocalDateTime.now();
        this.lastAccessedTime = this.sessionCreationTime;
    }

    //fixme session中存储了太多无用的数据
    private String sessionId;
    private Integer userId;
    private Integer thirdPlatformUserId;
    private Boolean isAdmin;
    private Boolean needDelete;
    private LocalDateTime sessionCreationTime;
    private LocalDateTime lastAccessedTime;
    private String avaterUrl;
    private String userName;
    private String userEmail;
}