package com.what.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.pojo.GithubCallBackResponse;
import com.what.spring.pojo.PlatfromUser;
import com.what.spring.pojo.Result;
import com.what.spring.service.ThirdAuthService;
import com.what.spring.util.ThirdLoginStatus;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


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
        String redirectUrl = rootUrl + "/" + mainPageUrl + "?";
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
            response.sendRedirect(redirectUrl);
        }
    }
}
