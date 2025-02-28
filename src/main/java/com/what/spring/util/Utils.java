package com.what.spring.util;

import com.what.spring.Exception.StringEmptyOrNull;

public class Utils {
    static public void NoEmptyOrBlank(String str, String message) throws StringEmptyOrNull {
        if (str == null || str.isBlank()) {
            throw new StringEmptyOrNull(message);
        }
    }
}
