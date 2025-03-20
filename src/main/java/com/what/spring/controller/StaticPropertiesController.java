package com.what.spring.controller;

import com.what.spring.pojo.ResultResponse;
import com.what.spring.service.nginx.NginxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("properties")
public class StaticPropertiesController {

    @Autowired
    private NginxService nginxService;

    @GetMapping("defaultAvatars")
    public ResultResponse getDefaultAvatars() {
        List<String> avatarUrls = nginxService.getAllDefaultAvatarUrl();
        return ResultResponse.success(avatarUrls);
    }
}
