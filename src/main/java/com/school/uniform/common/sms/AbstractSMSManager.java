package com.school.uniform.common.sms;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;

public abstract class AbstractSMSManager {

    public SMSManagerResult verify(String[] phones) throws TencentCloudSDKException {
        if (phones == null || phones.length == 0) {
            return null;
        }
        return send(phones);
    }

    public SMSManagerResult.OneSMSResult verify(String phone) throws TencentCloudSDKException {
        return verify(new String[]{phone}).resolveOneSMSResult();
    }

    public abstract SMSManagerResult parseResponse (
            SendSmsResponse response,
            String[] phones,
            String[] codes
    );

    public abstract SMSManagerResult send(String[] phones) throws TencentCloudSDKException;

    public abstract String[] params(String[] phones);
}
