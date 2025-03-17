package com.what.spring.controller;

import com.what.spring.component.NginxProperties;
import com.what.spring.pojo.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("properties")
public class StaticPropertiesController {
    @Autowired
    private NginxProperties nginxProperties;

    @GetMapping("defaultAvatars")
    public ResultResponse getDefaultAvatars(HttpServletRequest request) {

    }
}
