package com.what.spring.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Data
@Configuration
@ConfigurationProperties(prefix = "thread-pool.cache")
public class CacheThreadPoolConfig {

    @Value("${thread-pool.cache.prefix}")
    private String prefix;

    @Value("${thread-pool.cache.corethread-number}")
    private Integer corethreadNumber;

    @Value("${thread-pool.cache.maxthread-number}")
    private Integer maxthreadNumber;

    @Value("${thread-pool.cache.keepalive-time}")
    private Integer keepaliveTime;

    @Value("${thread-pool.cache.blockque-len}")
    private Integer blockqueLen;

    @Bean("myCacheThreadPool")
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Cache");
        executor.setCorePoolSize(corethreadNumber);
        executor.setMaxPoolSize(maxthreadNumber);
        executor.setKeepAliveSeconds(keepaliveTime);
        executor.setQueueCapacity(blockqueLen);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }
}
