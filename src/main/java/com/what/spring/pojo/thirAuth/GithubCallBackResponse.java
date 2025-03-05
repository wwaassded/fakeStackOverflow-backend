package com.what.spring.pojo.thirAuth;

import com.what.spring.util.thirdAuth.ThirdLoginStatus;
import lombok.Data;

@Data
public class GithubCallBackResponse {
    private ThirdLoginStatus status;
    private String message;
    private boolean isSuccess;
}
