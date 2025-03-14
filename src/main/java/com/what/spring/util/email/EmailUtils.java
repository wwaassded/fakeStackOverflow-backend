package com.what.spring.util.email;

import jakarta.mail.MessagingException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EmailUtils {
    public static Object getEmailKeyObjectAndInit(Class<?> clazz) throws MessagingException {
        Object keyObject = null;
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            keyObject = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new MessagingException("邮件发送出错, 无法加载模板html的参数类");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new MessagingException("邮件发送出错, 无法初始化模板html的参数类");
        }
        return keyObject;
    }
}
