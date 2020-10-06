package com.school.uniform.service;

import com.alibaba.fastjson.JSONObject;
import com.school.uniform.common.CONST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class GetWxAccessCodeTask {
    @Autowired
    private RedisTemplate redisTemplate;

    public String accessCode() throws Exception {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        // 判断 redis 里有没有这个 key
        boolean hasKey = redisTemplate.hasKey("accessToken");
        if (hasKey) {
            //  有 就直接返回这个key
            return operations.get("accessToken");
        } else {
            //  没有 就重新 调用微信Api 拿
            String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                    + CONST.appId + "&secret=" + CONST.appSecret;
            URL url = new URL(accessTokenUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            //获取返回的字符
            InputStream inputStream = connection.getInputStream();
            int size = inputStream.available();
            byte[] bs = new byte[size];
            inputStream.read(bs);
            String message = new String(bs, "UTF-8");
            //获取access_token
            JSONObject jsonObject = JSONObject.parseObject(message);
            String accessToken = jsonObject.getString("access_token");
            String expires_in = jsonObject.getString("expires_in");
            // 写入缓存 我这里设定的 时间是 7180 秒 access_token 失效时间相差了一点
            operations.set("accessToken", accessToken, 7180, TimeUnit.SECONDS);
//            System.out.println("access_Token"+accessToken);
            return operations.get("accessToken");
        }

    }
}
