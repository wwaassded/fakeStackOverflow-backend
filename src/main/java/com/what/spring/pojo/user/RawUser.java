package com.what.spring.pojo.user;

import com.what.spring.pojo.thirAuth.PlatfromUser;
import lombok.Data;

@Data
public class RawUser {
    private Integer websiteId;
    private Integer thirdPartyId;
    private String userName;
    private String userEmail;
    private String avatarUrl;
    private Boolean isActive;
    private String created_at;

    public RawUser() {
    }

    public RawUser(PlatfromUser user) {
        websiteId = user.getWebsiteId();
        thirdPartyId = user.getThirdpartyId();
        userName = user.getUserName();
        userEmail = user.getUserEmail();
        avatarUrl = user.getAvatarUrl();
        isActive = user.getIs_active() == 1;
        created_at = user.getCreatedAt();
    }
}
