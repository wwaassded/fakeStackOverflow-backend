package com.what.spring.pojo.user;

import com.what.spring.pojo.thirAuth.PlatfromUser;
import lombok.Data;

@Data
public class ThirdPlatFromInfo {
    private Integer id;
    private String name;
    private String email;
    private String avatarUrl;

    public ThirdPlatFromInfo() {
    }

    public ThirdPlatFromInfo(UserSession userSession, PlatfromUser platfromUser) {
        avatarUrl = userSession.getAvaterUrl();
        name = platfromUser.getUserName();
        id = platfromUser.getThirdpartyId();
        email = platfromUser.getUserEmail();
    }
}
