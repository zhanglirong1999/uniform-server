package com.school.uniform.common.cos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource({"classpath:qcloud/qcloud-cos.properties"})
public class QCloudCOSConfig {

	@Value("${cos.appId}")
	public Integer appId;
	@Value("${cos.secretId}")
	public String secretId;
	@Value("${cos.secretKey}")
	public String secretKey;
	@Value("${cos.regionString}")
	public String regionString;

//	@Bean
//	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
//		return new PropertySourcesPlaceholderConfigurer();
//	}
}
