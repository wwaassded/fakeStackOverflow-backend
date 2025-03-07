package com.what.spring.controller;

import com.what.spring.pojo.Result;
import com.what.spring.pojo.user.UserSession;
import com.what.spring.service.user.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "myCacheThreadPool")
    private ThreadPoolExecutor cacheThreadPool;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public void getUserRawInfoById(@PathVariable("id") Integer websiteId, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        try {
            Future<Optional<UserSession>> futureWebSiteId = cacheThreadPool.submit(() -> userService.getWebSiteIdFromSession(httpServletRequest));
            Result result = userService.getRawUserInfo(websiteId, httpServletRequest, futureWebSiteId);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {

        }
    }

}
