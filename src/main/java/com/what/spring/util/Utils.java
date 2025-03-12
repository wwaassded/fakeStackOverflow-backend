package com.what.spring.util;

import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.pojo.user.UserSession;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

public class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    private static final int magicNumber = 4;
    private static final String magicPrefix = "SessionELF";
    private static final String magicSufix = "*&^%";

    static public void NoEmptyOrBlank(String str, String message) throws StringEmptyOrNull {
        if (str == null || str.isBlank()) {
            throw new StringEmptyOrNull("-1", message);
        }
    }

    static public String getSessionId(UserSession userSession) {
        return SecureUtil.md5(magicPrefix + userSession.getUserId().toString() + (userSession.getThirdPlatformUserId().toString() + magicSufix).substring(magicNumber));
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
                //TODO
            }
        }
    }
}
