package com.school.uniform.common.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:qcloud/qcloud-cos.properties"})
public class COSConfigurer {

//    @Value("${cos.appId}")
//    private String appId;
//
//    @Value("${cos.secretId}")
//    private String secretId;
//
//    @Value("${cos.secretKey}")
//    private String secretKey;
//
//    @Value("${cos.regionString}")
//    private String regionString;
//
//    @Value("${cos.bucketName}")
//    private String bucketName;
//
//    @Value("${cos.bucketPath}")
//    private String bucketPath;
//
//    @Bean
//    public COSHolder qCloudHolder() {
//        return new COSHolder(
//                appId, secretId, secretKey,
//                regionString, bucketName, bucketPath
//        );
//    }
//
//
//    @Data
//    @AllArgsConstructor
//    public static class COSHolder {
//
//        private String appId;
//        private String secretId;
//        private String secretKey;
//        private String regionString;
//
//        private Region region;
//        private COSCredentials cred;
//        private ClientConfig clientConfig;
//        private String bucketName;
//        private String baseUrl;
//
//        public COSHolder(
//                String appId,
//                String secretId,
//                String secretKey,
//                String regionString,
//                String bucketName,
//                String baseUrl
//        ) {
//            this.appId = appId;
//            this.secretId = secretId;
//            this.secretKey = secretKey;
//            this.regionString = regionString;
//            init(bucketName, baseUrl);
//        }
//
//        public void init(
//                String bucketName,
//                String baseUrl
//        ) {
//            this.region = new Region(regionString);
//            this.cred = new BasicCOSCredentials(secretId, secretKey);
//            this.clientConfig = new ClientConfig(this.region);
//            this.bucketName = bucketName;
//            this.baseUrl = baseUrl;
//        }
//
//        public COSClient newCOSClient() {
//            return new COSClient(this.cred, this.clientConfig);
//        }
//
//        public void closeCOSClient(COSClient cosClient) {
//            cosClient.shutdown();
//        }
//    }

}
