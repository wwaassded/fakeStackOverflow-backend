package com.what.spring.controller;

import cn.hutool.log.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.pojo.thirAuth.GithubCallBackResponse;
import com.what.spring.pojo.thirAuth.PlatfromUser;
import com.what.spring.pojo.Result;
import com.what.spring.pojo.user.SessionCounter;
import com.what.spring.pojo.user.UserSession;
import com.what.spring.service.thirdAuth.ThirdAuthService;
import com.what.spring.util.Utils;
import com.what.spring.util.thirdAuth.ThirdLoginStatus;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


@RestController
@PropertySource("classpath:application.yaml")
@RequestMapping("login")
public class LoginController {

    @Value("${frontend.root}")
    private String rootUrl;

    @Value("${frontend.mainPage}")
    private String mainPageUrl;

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Resource(name = "myNetWorkIOThreadPool")
    private ThreadPoolExecutor ioThreadPool;

    @Resource(name = "myCacheThreadPool")
    private ThreadPoolExecutor cacheThreadPool;

    @Resource(name = "githubThirdAuthService")
    private ThirdAuthService githubThirdAuthService;

    @Resource(name = "myObjectMapper")
    private ObjectMapper objectMapper;

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "sessionCache")
    private ConcurrentHashMap<String, SessionCounter> concurrentHashMap;

    @GetMapping("thirdAuth/github")
    public void githubThirdAuthCallBack(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getAttribute("userSession") != null) {
            LOG.error(request.getAttribute("userSession").toString());
        }
        Future<Result> futureResult = ioThreadPool.submit(() -> githubThirdAuthService.thirdAuthHandle(code));
        GithubCallBackResponse githubCallBackResponse = new GithubCallBackResponse();
        UserSession userSession = new UserSession(false);
        StringBuilder redirectUrl = new StringBuilder();
        redirectUrl.append(rootUrl).append(mainPageUrl).append('?');
        try {
            Result result = futureResult.get();
            LOG.info(result.getMessage());
            if (result.getIsSuccessful()) {
                PlatfromUser platfromUser = (PlatfromUser) result.getObject();
                if (platfromUser.getUserPassword() == null) {
                    githubCallBackResponse.setStatus(ThirdLoginStatus.SUCCES_NEED_CREATE);
                } else {
                    githubCallBackResponse.setStatus(ThirdLoginStatus.SUCCES);
                }
                githubCallBackResponse.setUserId(platfromUser.getWebsiteId());
                userSession.fillUserInfo(platfromUser);
                String jsonSession = objectMapper.writeValueAsString(userSession);
                String sessionId = Utils.getSessionId(userSession);
                Cookie sessionCookie = Utils.getGlobalCookie("sessionId", sessionId, 3600);
                response.addCookie(sessionCookie);
                cacheThreadPool.execute(() -> redisTemplate.opsForValue().set(sessionId, jsonSession, Duration.ofHours(1)));
                concurrentHashMap.computeIfAbsent(sessionId, k -> new SessionCounter(userSession, 1));
            } else {
                githubCallBackResponse.setStatus(ThirdLoginStatus.SUCCES);
            }
        } catch (DataAccessException e) {
            LOG.error(e.getMessage());
            if (e instanceof DuplicateKeyException de) {
                githubCallBackResponse.setStatus(ThirdLoginStatus.DUPLICATE);
                // 用户重复创建关联统一第三方用户的账号 一般不会出现这个情况
            } else {
                githubCallBackResponse.setStatus(ThirdLoginStatus.SQLERROR);
            }
        } catch (StringEmptyOrNull e) {
            LOG.error(e.getMessage());
            githubCallBackResponse.setStatus(ThirdLoginStatus.GET_NO_USE_STRING);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            redirectUrl.append("status").append("=").append(githubCallBackResponse.getStatus()).append('&');
            redirectUrl.append("id").append("=").append(githubCallBackResponse.getUserId());
            response.sendRedirect(redirectUrl.toString());
        }
    }
}
