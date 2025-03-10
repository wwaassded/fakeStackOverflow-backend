package com.what.spring.pojo.user;

import lombok.Data;

@Data
public class UserRawInfoResponse {
    private Boolean isSuccess;
    private String rawUserInfo;
}
