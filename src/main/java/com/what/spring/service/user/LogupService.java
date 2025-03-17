package com.what.spring.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.what.spring.DTO.Validate;
import com.what.spring.mapper.UserMapper;
import com.what.spring.pojo.MailDTO;
import com.what.spring.pojo.thirAuth.PlatfromUser;
import com.what.spring.pojo.user.NameAndPassword;
import com.what.spring.pojo.user.UserSession;
import com.what.spring.service.email.MailService;
import com.what.spring.util.Utils;
import com.what.spring.util.email.EmailUtils;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@ConfigurationProperties(prefix = "nginx")
public class LogupService {

    @Value("${nginx.root}")
    private String nginxRoot;

    @Value("${nginx.avatar.root}")
    private String nginxAvatarRoot;

    @Value("${nginx.avatar.defaultAvatar}")
    private String defaultAvatarPng;

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "myCacheThreadPool")
    private ThreadPoolExecutor cacheThreadPoolExecutor;

    @Resource(name = "mailService")
    private MailService mailService;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private SessionService sessionService;

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

    public Boolean searchKeyAndCreateUser(String key, HttpServletResponse response) throws JsonProcessingException {
        NameAndPassword nameAndPassword = (NameAndPassword) redisTemplate.opsForValue().getAndDelete(key);
        if (nameAndPassword == null) {
            return false;
        }
        PlatfromUser platfromUser = new PlatfromUser();
        platfromUser.initByNameAndPassword(nameAndPassword, nginxRoot + nginxAvatarRoot + defaultAvatarPng);
        userMapper.insertPlatformUserWithDefaultTime(platfromUser);
        if (platfromUser.getWebsiteId() == 0) {
            return false;
        }
        sessionService.userSessionHandle(platfromUser, response);
        return true;
    }
}
