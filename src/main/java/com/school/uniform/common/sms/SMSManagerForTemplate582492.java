package com.school.uniform.common.sms;

import com.school.uniform.common.config.qcloud.QCloudSMSConfigurer;
import com.school.uniform.util.IDUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SMSManagerForTemplate582492 extends AbstractSMSManager {

    public static final String DOMESTIC = "+86";

    @Autowired
    private Credential credential;

    @Autowired
    private QCloudSMSConfigurer.SMSConfigHolder smsConfigHolder;


    @Override
    public SMSManagerResult parseResponse(SendSmsResponse response, String[] phones, String[] codes) {
        SMSManagerResult result = new SMSManagerResult();
        SendStatus[] sendStatusSet = response.getSendStatusSet();
        int n = phones.length;
        for (int i = 0; i < n; ++i) {
            String phone = phones[i];
            String code = codes[i];
            SendStatus sendStatus = sendStatusSet[i];
            String status = sendStatus.getCode();
            String message = sendStatus.getMessage();
            result.addOneSMSResult(
                    new SMSManagerResult.OneSMSResult(
                            phone, code, status, message
                    )
            );
        }
        return result;
    }

    @Override
    public SMSManagerResult send(String[] phones) throws TencentCloudSDKException {
        SmsClient smsClient = new SmsClient(credential, "");
        SendSmsRequest request = new SendSmsRequest();

        // appId
        request.setSmsSdkAppid(smsConfigHolder.getAppId());

        // 签名内容
        request.setSign(smsConfigHolder.getSignContent());

        // template id
        request.setTemplateID(smsConfigHolder.getTemplateId());

        // 填入手机号
        String[] tempPhones = new String[phones.length];
        for (int i = 0; i < phones.length; ++i) {
            tempPhones[i] = DOMESTIC + phones[i];
        }
        request.setPhoneNumberSet(tempPhones);

        // 填入模板参数
        String[] params = params(phones);
        request.setTemplateParamSet(params);

        // 返回结果
        SendSmsResponse sendSmsResponse = smsClient.SendSms(request);
        return parseResponse(sendSmsResponse, phones, params);
    }

    @Override
    public String[] params(String[] phones) {
        String[] params = new String[phones.length];
        for (int i = 0; i < phones.length; ++i) {
            // 生成 6 位可重复数字验证码
            params[i] =
//                String.format("【%s】", smsConfigHolder.getSignContent()) +
                    IDUtil.getRepeatableVerifyNumberCode(6);
        }
        return params;
    }
}

