package com.school.uniform.common.cos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QCloudActivityConfig {

	@Autowired
	private QCloudCOSConfig qCloudCOSConfig;
	@Value("${cos.bucketName}")
	private String bucketName;
	@Value("${cos.bucketPath}")
	private String bucketPath;

	@Bean
	public QCloudHolder qCloudCOSClientHolder() {
		return new QCloudHolder(this.qCloudCOSConfig, this.bucketName, this.bucketPath);
	}
}