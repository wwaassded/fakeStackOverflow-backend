package com.what.spring.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Data
@Configuration
@ConfigurationProperties(prefix = "thread-pool.network-io")
public class NetWorkIOThreadPoolConfig {

    @Value("${thread-pool.network-io.prefix}")
    private String prefix;

    @Value("${thread-pool.network-io.corethread-number}")
    private Integer corethreadNumber;

    @Value("${thread-pool.network-io.maxthread-number}")
    private Integer maxthreadNumber;

    @Value("${thread-pool.network-io.keepalive-time}")
    private Integer keepaliveTime;

    @Value("${thread-pool.network-io.blockque-len}")
    private Integer blockqueLen;

    @Bean("myNetWorkIOThreadPool")
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("NetWorkIO");
        executor.setCorePoolSize(corethreadNumber);
        executor.setMaxPoolSize(maxthreadNumber);
        executor.setKeepAliveSeconds(keepaliveTime);
        executor.setQueueCapacity(blockqueLen);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }
}
