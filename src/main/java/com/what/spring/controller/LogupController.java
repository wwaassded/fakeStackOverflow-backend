package com.what.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.what.spring.DTO.Validate;
import com.what.spring.pojo.ResultResponse;
import com.what.spring.pojo.user.NameAndPassword;
import com.what.spring.service.user.LogupService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@RequestMapping("logup")
@RestController
public class LogupController {

    @Value("${backend.root}")
    private String root;

    @Value("${backend.logup-verification}")
    private String verificationSuffix;

    @Resource
    private LogupService logupService;

    @PostMapping
    public void logupHandler(@Valid @RequestBody NameAndPassword nameAndPassword, HttpServletRequest request, HttpServletResponse response) throws MessagingException {
        nameAndPassword.getMd5();
        String key = logupService.cacheNameAndPassword(nameAndPassword);
        Validate validate = new Validate();
        String url = root + verificationSuffix + key;
        validate.init(nameAndPassword, url);
        logupService.sendValidateEmial(validate);
    }

    @PostMapping("verification")
    public ResultResponse logupVerification(@RequestParam String key, HttpServletRequest request, HttpServletResponse response) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            if (logupService.searchKeyAndCreateUser(key, response)) {
                resultResponse.setResultMsg("注册成功已经可以登陆了");
            } else {
                resultResponse.setResultMsg("注册失败,验证邮件已经过期请重新注册");
                resultResponse.setResultCode("200209");
            }
        } catch (JsonProcessingException e) {
            resultResponse.setResultCode(e.getMessage());
            resultResponse.setResultMsg("在将usession转换成json格式时出现了错误");
        }
        return resultResponse;
    }
}
