package com.what.spring.service;

import com.what.spring.component.util.MailProperties;
import com.what.spring.pojo.MailDTO;
import jakarta.annotation.Resource;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class MailService {

    @Resource
    private MailProperties mailProperties;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private Mapper mapper;

    @Resource(name = "myNetWorkIOThreadPool")
    private ThreadPoolExecutor threadPoolExecutor;

    public Future<Integer> sendSimpleMailMessage(MailDTO mailDTO) {
        if (!StringUtils.hasLength(mailDTO.getFrom())) {
            mailDTO.setFrom(mailProperties.getFrom());
        }
        SimpleMailMessage simpleMailMessage = mapper.map(mailDTO, SimpleMailMessage.class);
        return threadPoolExecutor.submit(() -> {
            try {
                javaMailSender.send(simpleMailMessage);
            } catch (MailException e) {
                return -1;
            }
            return 0;
        });
    }

}
