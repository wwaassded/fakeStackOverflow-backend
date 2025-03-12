package com.what.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.pojo.user.NameAndPassword;
import com.what.spring.service.user.LogupService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("logup")
@RestController
public class LogupController {

    @Resource(name = "myObjectMapper")
    private ObjectMapper objectMapper;

    @Resource
    private LogupService logupService;

    @PostMapping
    public void logupHandler(@Valid @RequestBody NameAndPassword nameAndPassword, HttpServletRequest request, HttpServletResponse response) {
        nameAndPassword.getMd5();
        String key = logupService.cacheNameAndPassword(nameAndPassword);
        logupService.emailHandler(nameAndPassword.getEmail(), key);
        // TODO 进行邮箱验证逻辑
    }
}
