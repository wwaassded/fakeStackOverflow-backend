package com.what.spring.controller;

import com.what.spring.DTO.Validate;
import com.what.spring.pojo.user.NameAndPassword;
import com.what.spring.service.user.LogupService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public void logupVerification(@RequestParam String key) {

    }
}
