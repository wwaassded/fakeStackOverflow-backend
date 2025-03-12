package com.what.spring.component.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
@Data
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    @Value("${mail.domain}")
    private String domain;

    @Value("${mail.from}")
    private String from;

}
