package com.what.spring.service.user;

import com.what.spring.DTO.Validate;
import com.what.spring.pojo.MailDTO;
import com.what.spring.pojo.user.NameAndPassword;
import com.what.spring.service.email.MailService;
import com.what.spring.util.Utils;
import com.what.spring.util.email.EmailUtils;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    public void sendValidateEmial(Validate validate) throws MessagingException {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setTo(new String[]{validate.getEmail()});
        Map<String, Object> contex = EmailUtils.getEmailKeyContextFromObject(validate);
        String templateKey = validate.getClass().getName().toLowerCase();
        mailService.sendMimeMessage(mailDTO, contex, templateKey);
    }
}
