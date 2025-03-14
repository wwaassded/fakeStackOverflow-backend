package com.what.spring.service.user;

import com.what.spring.pojo.MailDTO;
import com.what.spring.pojo.user.NameAndPassword;
import com.what.spring.service.email.MailService;
import com.what.spring.util.Utils;
import com.what.spring.util.email.EmailUtils;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class LogupService {

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "myCacheThreadPool")
    private ThreadPoolExecutor cacheThreadPoolExecutor;

    @Resource(name = "mailService")
    private MailService mailService;

    public String cacheNameAndPassword(NameAndPassword nameAndPassword) {
        String key = Utils.getNAPKey(nameAndPassword);
        cacheThreadPoolExecutor.execute(() -> redisTemplate.opsForValue().set(key, nameAndPassword, Duration.ofMinutes(1)));
        return key;
    }

    public void emailHandler(String email, Class<?> Emailclass, String key) throws MessagingException {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo(new String[]{email});
        Map<String, Object> contex = new HashMap<>();
        Object keyObject = EmailUtils.getEmailKeyObjectAndInit(Emailclass);
        if (keyObject == null) {
            throw new MessagingException("无法获取邮件参数对象 未知错误");
        }
        String[] split = Emailclass.getName().split("\\.");
        String templateKey = split[split.length - 1].toLowerCase();
        mailService.sendMimeMessage(mailDTO, contex, templateKey);
    }
}
