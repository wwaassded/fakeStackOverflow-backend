package com.what.spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.pojo.Result;
import com.what.spring.pojo.ResultResponse;
import com.what.spring.pojo.thirAuth.PlatfromUser;
import com.what.spring.pojo.user.NameAndPassword;
import com.what.spring.pojo.user.RawUser;
import com.what.spring.pojo.user.UserRawInfoResponse;
import com.what.spring.pojo.user.UserSession;
import com.what.spring.service.user.UserService;
import com.what.spring.util.Utils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "myObjectMapper")
    private ObjectMapper objectMapper;

    @PostMapping("createNewUserOnThirdPlatformUser")
    public ResultResponse createNewUserOnThirdPlatformUser(@Valid @RequestBody NameAndPassword nameAndPassword, HttpServletRequest request) {
        UserSession userSession = (UserSession) request.getAttribute("userSession");
        userService.createUserOnThirdPlatformUser(userSession, nameAndPassword);
    }

    @PostMapping("avatar")
    public void setUserAvatar(@RequestParam String avatarUrl, HttpServletRequest request) {
        UserSession userSession = (UserSession) request.getAttribute("userSession");
        userService.setUserAvatar(userSession, avatarUrl);
    }

    @GetMapping
    public void getUserRawInfoById(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        UserRawInfoResponse infoResponse = new UserRawInfoResponse();
        try {
            UserSession userSession = (UserSession) httpServletRequest.getAttribute("userSession");
            Result result = userService.getRawUserInfo(userSession);
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
