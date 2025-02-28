package com.what.spring.service;

import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.pojo.Result;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class ThirdAuthService {
    protected final WebClient webClient;

    ThirdAuthService(WebClient webClient) {
        this.webClient = webClient;
    }

    public abstract Result thirdAuthHandle(String thirdAuthCode) throws StringEmptyOrNull;
}
