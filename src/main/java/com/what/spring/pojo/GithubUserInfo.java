package com.what.spring.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GithubUserInfo implements ThirdPlatformUserInfo {

    @JsonProperty("id")
    private Long userId;

    @JsonProperty("avatar_id")
    private String avatarURL;

    private String email;

    @JsonProperty("html_url")
    private String htmlURL;

}
