package com.what.spring.util;

import com.what.spring.Exception.StringEmptyOrNull;
import jakarta.servlet.http.Cookie;


import java.time.LocalDateTime;

import static cn.hutool.core.util.StrUtil.uuid;

public class Utils {
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
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(maxAge);
        return sessionCookie;
    }
}
