package com.school.uniform.util;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class IDUtil {

    public static String getRepeatableVerifyNumberCode(int len) {
        // 生成六位可重复数字串
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; ++i) {
            stringBuilder.append(random.nextInt(65535) % 10);
        }
        return stringBuilder.toString();
    }

    public static String buildBase64ByMD5(String s) throws NoSuchAlgorithmException {
        if (s == null || s.equals("")) {
            return s;
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Base64.encodeBase64String(md5.digest(s.getBytes(StandardCharsets.UTF_8)));
    }

    public static String buildVerifyCodeByMD5(
            String phone, String username, String verifyCode
    ) throws NoSuchAlgorithmException {
        String raw = phone + ":" + username + ":" + verifyCode;
        return buildBase64ByMD5(raw);
    }
}

