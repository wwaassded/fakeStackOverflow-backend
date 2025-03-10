package com.what.spring.pojo.thirAuth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmailInfo {
    @JsonProperty("email")
    private String email;
    private Boolean primary;
    private Boolean verified;
    private String visibility;
}
