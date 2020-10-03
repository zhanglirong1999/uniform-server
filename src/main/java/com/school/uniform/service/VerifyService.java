package com.school.uniform.service;

import com.school.uniform.model.dto.post.PhoneUsernameCodeVerifier;
import com.school.uniform.model.dto.post.PhoneUsernameVerifier;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Validated
public interface VerifyService {
    String getRequestIP( HttpServletRequest request);

    void postVerifyCode(
             HttpServletRequest request,
             PhoneUsernameVerifier phoneUsernameVerifier
    ) throws Exception;

    String buildErrorIPKey(
             String ip
    );

    String buildPhoneUserVerifyCodeKey(
             String phone,  String username
    );

    Map.Entry<String, String> buildSuccessfulVerifyCodeEntry(
             String phone,
             String username,
             String verifyCode
    ) throws NoSuchAlgorithmException;

    String postLogin(
             HttpServletRequest request,
             PhoneUsernameCodeVerifier verifier
    ) throws NoSuchAlgorithmException;

    boolean isPossibleVerifyCode(String verifyCode);

    String buildTokenKey(
             String verifyCodeValue
    );

    String getTokenValueInRedis( String resultToken);

    void deleteTokenKeyInRedis( String resultToken);
}
