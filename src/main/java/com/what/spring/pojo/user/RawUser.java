package com.what.spring.pojo.user;

import com.what.spring.pojo.thirAuth.PlatfromUser;
import lombok.Data;

@Data
public class RawUser {
    private Integer websiteId;
    private Integer thirdPartyId;
    private String userName;
    private String UserEmail;
    private String avatarUrl;
    private Boolean isActive;
    private String created_at;

    public RawUser() {
    }

    public RawUser(PlatfromUser user) {

    }
}
