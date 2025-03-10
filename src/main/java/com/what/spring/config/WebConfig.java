package com.what.spring.config;

import com.what.spring.interceptor.SessionPermissionsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionPermissionsInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login/**");
    }
}
