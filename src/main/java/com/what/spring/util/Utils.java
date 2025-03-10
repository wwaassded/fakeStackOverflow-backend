package com.what.spring.util;

import cn.hutool.log.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.Exception.FailToWriteErrorMessageToResponse;
import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.controller.UserController;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.time.LocalDateTime;

import static cn.hutool.core.util.StrUtil.uuid;

public class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    static public void NoEmptyOrBlank(String str, String message) throws StringEmptyOrNull {
        if (str == null || str.isBlank()) {
            throw new StringEmptyOrNull(message);
        }
    }

    static public String getSessionId(String code) {
        return LocalDateTime.now() + uuid() + code;
    }

    static public Cookie getGlobalCookie(String key, String value, Integer maxAge) {
        Cookie sessionCookie = new Cookie(key, value);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(maxAge);
        return sessionCookie;
    }

    static public void responseToServlet(HttpServletResponse response, Object o, ObjectMapper objectMapper) {
        try {
            String jsonString = objectMapper.writeValueAsString(o);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonString);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"isSuccess\":false,\"message\":\"json io error\"}");
            } catch (IOException ee) {
                LOG.error(ee.getMessage());
                throw new FailToWriteErrorMessageToResponse("无法向客户端返回错误信息");
            }
        }
    }
}
