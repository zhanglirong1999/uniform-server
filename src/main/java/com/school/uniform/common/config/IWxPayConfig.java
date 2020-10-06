package com.school.uniform.common.config;

import com.github.wxpay.sdk.WXPayConfig;
import com.school.uniform.common.CONST;
import com.school.uniform.common.wxpay.IWXPayDomain;
import com.school.uniform.common.wxpay.WXPayConstants;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
@Service

public class IWxPayConfig implements WXPayConfig {
    private byte[] certData;

    @Override
    public String getAppID() {
        return CONST.appId;
    }

    @Override
    public String getMchID() {
        return CONST.mch_id;
    }

    @Override
    public String getKey() {
        return CONST.appSecret;
    }

    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    public IWXPayDomain getWXPayDomain() { // 这个方法需要这样实现, 否则无法正常初始化WXPay
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(com.school.uniform.common.wxpay.WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }

        };
        return iwxPayDomain;
    }

}
