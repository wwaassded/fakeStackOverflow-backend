package com.what.spring.controller;

import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.pojo.Result;
import com.what.spring.service.ThirdAuthService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("login")
public class LoginController {

    @Resource(name = "githubThirdAuthService")
    private ThirdAuthService thirdAuthService;

    @GetMapping("callback")
    public Object thirdAuthCallBack(@RequestParam("code") String code) {
        //TODO 调用service层的组件
        //TODO 需要自定义一个相应对象
        try {
            Result result = thirdAuthService.thirdAuthHandle(code);
            if (result.getIsSuccessful()) {

            }
        } catch (StringEmptyOrNull e) {
            System.out.println(e.getMessage());
        } finally {

        }
    }

}
