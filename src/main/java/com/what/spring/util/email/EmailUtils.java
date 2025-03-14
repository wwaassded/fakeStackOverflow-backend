package com.what.spring.util.email;

import com.what.spring.myInterface.email.Atrribute;
import jakarta.mail.MessagingException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class EmailUtils {
    public static Map<String, Object> getEmailKeyContextFromObject(Object o) throws MessagingException {
        TreeMap<String, Object> map = new TreeMap<>();
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (var field : fields) {
                field.setAccessible(true);
                String key = field.getName();
                Atrribute atrribute = field.getAnnotation(Atrribute.class);
                if (atrribute != null) {
                    key = atrribute.value();
                }
                map.put(key, field.get(o));
            }
        } catch (IllegalAccessException e) {
            throw new MessagingException("服务器发送邮件中发生错误 无法获取参数类的值");
        }
        return map;
    }
}
