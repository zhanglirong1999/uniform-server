package com.school.uniform.common.config;

import com.school.uniform.service.fail.ActivityFailPrompt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ServiceFailPromptConfig {

    @Bean
    public ActivityFailPrompt activityFailPrompt() throws IOException {
        return new ActivityFailPrompt("qcloud/fail-cos.properties");
    }

}
