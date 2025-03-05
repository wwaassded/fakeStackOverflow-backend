package com.what.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.pojo.thirAuth.GithubCallBackResponse;
import com.what.spring.pojo.thirAuth.PlatfromUser;
import com.what.spring.pojo.Result;
import com.what.spring.service.thirdAuth.ThirdAuthService;
import com.what.spring.util.thirdAuth.ThirdLoginStatus;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Field;


@RestController
@PropertySource("classpath:application.yaml")
@RequestMapping("login")
public class LoginController {

    @Value("${frontend.root}")
    private String rootUrl;

    @Value("${frontend.mainPage}")
    private String mainPageUrl;

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Resource(name = "githubThirdAuthService")
    private ThirdAuthService githubThirdAuthService;

    @Resource(name = "myObjectMapper")
    private ObjectMapper objectMapper;

    @GetMapping("thirdAuth/github")
    public void githubThirdAuthCallBack(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        //TODO 调用service层的组件
        //TODO 需要自定义一个相应对象
        GithubCallBackResponse githubCallBackResponse = new GithubCallBackResponse();
        StringBuilder redirectUrl = new StringBuilder();
        redirectUrl.append(rootUrl).append('/').append(mainPageUrl).append('?');
        try {
            Result result = githubThirdAuthService.thirdAuthHandle(code);
            LOG.info(result.getMessage());
            if (result.getIsSuccessful()) {
                githubCallBackResponse.setSuccess(true);
                githubCallBackResponse.setMessage("success");
                PlatfromUser platfromUser = (PlatfromUser) result.getObject();
                if (platfromUser.getUserPassword() == null) {
                    githubCallBackResponse.setStatus(ThirdLoginStatus.SUCCES_NEED_CREATE);
                } else {
                    githubCallBackResponse.setStatus(ThirdLoginStatus.SUCCES);
                }
            } else {
                githubCallBackResponse.setSuccess(false);
                githubCallBackResponse.setMessage("fail without error: " + result.getTroubleMaker());
                githubCallBackResponse.setStatus(ThirdLoginStatus.SUCCES);
            }
        } catch (DataAccessException e) {
            LOG.error(e.getMessage());
            githubCallBackResponse.setSuccess(false);
            if (e instanceof DuplicateKeyException de) {
                githubCallBackResponse.setMessage("重复创建账号");
                githubCallBackResponse.setStatus(ThirdLoginStatus.DUPLICATE);
                // 用户重复创建关联统一第三方用户的账号 一般不会出现这个情况
            } else {
                githubCallBackResponse.setMessage("sql出现错误 " + e.getMessage());
                githubCallBackResponse.setStatus(ThirdLoginStatus.SQLERROR);
            }
        } catch (StringEmptyOrNull e) {
            LOG.error(e.getMessage());
            githubCallBackResponse.setSuccess(false);
            githubCallBackResponse.setStatus(ThirdLoginStatus.GET_NO_USE_STRING);
            githubCallBackResponse.setMessage(e.getMessage());
        } finally {
            redirectUrl.append("status").append("=").append(githubCallBackResponse.getStatus()).append('&');
            redirectUrl.append("message").append("=").append(githubCallBackResponse.getMessage()).append('&');
            redirectUrl.append("isSuccess").append("=").append(githubCallBackResponse.isSuccess());
            response.sendRedirect(redirectUrl.toString());
        }
    }
}
