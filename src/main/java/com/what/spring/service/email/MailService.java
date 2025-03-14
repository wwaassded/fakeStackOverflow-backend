package com.what.spring.service.email;

import com.what.spring.component.util.MailProperties;
import com.what.spring.pojo.MailDTO;
import jakarta.annotation.Resource;
import com.github.dozermapper.core.Mapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Map;
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

    @Resource
    private TemplateEngine templateEngine;

    public void sendSimpleMailMessage(MailDTO mailDTO) {
        if (!StringUtils.hasLength(mailDTO.getFrom())) {
            mailDTO.setFrom(mailProperties.getFrom());
        }
        SimpleMailMessage simpleMailMessage = mapper.map(mailDTO, SimpleMailMessage.class);
        javaMailSender.send(simpleMailMessage);
    }

    public void sendMimeMessage(MailDTO mailDTO, Map<String, Object> templateAtrribute, String templateKey) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        messageHelper = new MimeMessageHelper(mimeMessage, true);
        if (!StringUtils.hasLength(mailDTO.getFrom())) {
            messageHelper.setFrom(mailProperties.getFrom());
        }
        messageHelper.setTo(mailDTO.getTo());
        messageHelper.setSubject(mailDTO.getSubject());
        mimeMessage = messageHelper.getMimeMessage();
        MimeMultipart mimeMultipart = getMimeMultipart(mailDTO, templateAtrribute, templateKey);
        mimeMessage.setContent(mimeMultipart);
        mimeMessage.saveChanges();
        javaMailSender.send(mimeMessage);
    }

    private MimeMultipart getMimeMultipart(MailDTO mailDTO, Map<String, Object> templateAtrribute, String templateKey) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        Context context = new Context();
        context.setVariables(templateAtrribute);
        String htmlContent = templateEngine.process(templateKey, context);
        mimeBodyPart.setContent(htmlContent, "text/html;charset=UTF-8");
        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.setSubType("related");
        mimeMultipart.addBodyPart(mimeBodyPart);
        for (String filename : mailDTO.getFilenames()) {
            MimeBodyPart bodyPart = new MimeBodyPart();
            try {
                bodyPart.attachFile(filename);
            } catch (IOException e) {
                throw new MessagingException("找不到您所描述的附件");
            }
            mimeMultipart.addBodyPart(bodyPart);
        }
        return mimeMultipart;
    }

}
