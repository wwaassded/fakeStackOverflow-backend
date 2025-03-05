package com.what.spring.pojo.thirAuth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GithubUserInfo implements ThirdPlatformUserInfo {

    @JsonProperty("id")
    private Integer userId;

    @JsonProperty("avatar_url")
    private String avatarURL;

    private String email;

    @JsonProperty("html_url")
    private String htmlURL;

    @JsonProperty("name")
    private String userName;

}
