package com.what.spring.config;

import com.what.spring.interceptor.SessionPermissionsInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource(name = "sessionPermissionsInterceptor")
    private SessionPermissionsInterceptor sessionPermissionsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionPermissionsInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login/**");
    }
}
