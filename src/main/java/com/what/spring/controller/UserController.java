package com.what.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.pojo.Result;
import com.what.spring.pojo.thirAuth.PlatfromUser;
import com.what.spring.pojo.user.RawUser;
import com.what.spring.pojo.user.UserRawInfoResponse;
import com.what.spring.pojo.user.UserSession;
import com.what.spring.service.user.UserService;
import com.what.spring.util.Utils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequestMapping("user/{id}")
public class UserController {

    private final UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "myCacheThreadPool")
    private ThreadPoolExecutor cacheThreadPool;

    @Resource(name = "myObjectMapper")
    private ObjectMapper objectMapper;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public void getUserRawInfoById(@PathVariable("id") Integer websiteId, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        UserRawInfoResponse infoResponse = new UserRawInfoResponse();
        try {
            Future<Optional<UserSession>> futureWebSiteId = cacheThreadPool.submit(() -> userService.getWebSiteIdFromSession(httpServletRequest));
            Result result = userService.getRawUserInfo(websiteId, httpServletRequest, futureWebSiteId);
            if (result.getIsSuccessful()) {
                if (result.getObject() instanceof PlatfromUser rawUer) {
                    RawUser user = new RawUser(rawUer);
                    String jsonUser = objectMapper.writeValueAsString(user);
                    infoResponse.setRawUserInfo(jsonUser);
                    infoResponse.setIsSuccess(true);
                } else {
                    throw new RuntimeException("Unknown error object should be PlatformUser");
                }
            } else {
                infoResponse.setIsSuccess(false);
                infoResponse.setRawUserInfo(result.getMessage());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            infoResponse.setIsSuccess(false);
            infoResponse.setRawUserInfo(e.getMessage());
        } finally {
            Utils.responseToServlet(response, infoResponse, objectMapper);
        }
    }

}
