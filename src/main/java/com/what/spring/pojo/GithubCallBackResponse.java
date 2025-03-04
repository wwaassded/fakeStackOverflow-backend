package com.what.spring.pojo;

import com.what.spring.util.ThirdLoginStatus;
import lombok.Data;

@Data
public class GithubCallBackResponse {
    private ThirdLoginStatus status;
    private String message;
    private boolean isSuccess;
}
