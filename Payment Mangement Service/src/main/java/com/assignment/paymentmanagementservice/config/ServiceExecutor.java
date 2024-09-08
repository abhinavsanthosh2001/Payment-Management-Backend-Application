package com.assignment.paymentmanagementservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ServiceExecutor implements AsyncConfigurer {
    @Override
    @Bean("Async")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setThreadNamePrefix("Async");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
