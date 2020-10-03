package com.school.uniform.common.config.qcloud;

import com.tencentcloudapi.common.Credential;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {
        "classpath:qcloud/qcloud-sms.properties"
}, encoding = "UTF-8")
public class QCloudSMSConfigurer {

    @Value("${sms.secretId}")
    private String secretId;

    @Value("${sms.secretKey}")
    private String secretKey;

    @Value("${sms.appId}")
    private String smsAPPId;

    @Value("${sms.signContent}")
    private String smsSignContent;

    @Value("${sms.templateId}")
    private String smsTemplateId;

    @Bean
    public Credential credential() {
        return new Credential(secretId, secretKey);
    }

    @Bean
    public SMSConfigHolder smsConfigHolder() {
        return new SMSConfigHolder(smsAPPId, smsSignContent, smsTemplateId);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SMSConfigHolder {
        private String appId;
        private String signContent;
        private String templateId;
    }

}
