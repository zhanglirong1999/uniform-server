package com.school.uniform.service.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.school.uniform.common.CONST;
import com.school.uniform.common.config.IWxPayConfig;
import com.school.uniform.common.wxpay.WXPay;
import com.school.uniform.common.wxpay.WXPayUtil;
import com.school.uniform.exception.BizException;
import com.school.uniform.service.WxPayService;
import com.school.uniform.util.ConstantUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    private IWxPayConfig iWxPayConfig;

    @Override
    public Object unifiedOrder(String orderNo, double amount,String openId,String ip) throws Exception {
        Map<String, String> returnMap = new HashMap<>();
        Map<String, String> responseMap = new HashMap<>();
        Map<String, String> requestMap = new HashMap<>();
        String detail ="购买校服";
        try {
            WXPay wxpay = new WXPay(iWxPayConfig);
            requestMap.put("body", "购买校服");                                     // 商品描述
            requestMap.put("out_trade_no", orderNo);                          // 商户订单号
            requestMap.put("total_fee", String.valueOf((int)(amount*100)));   // 总金额
            requestMap.put("spbill_create_ip", ip); // 终端IP
            requestMap.put("trade_type", "JSAPI");                              // App支付类型
            requestMap.put("notify_url", CONST.notify_url);   // 接收微信支付异步通知回调地址
            Map<String, String> resultMap = wxpay.unifiedOrder(requestMap);
            //获取返回码
            String returnCode = resultMap.get("return_code");
            String returnMsg = resultMap.get("return_msg");
            //若返回码为SUCCESS，则会返回一个result_code,再对该result_code进行判断
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = resultMap.get("result_code");
                String errCodeDes = resultMap.get("err_code_des");
                if ("SUCCESS".equals(resultCode)) {
                    responseMap = resultMap;
                }
            }
            if (responseMap == null || responseMap.isEmpty()) {
                throw new BizException(ConstantUtil.BizExceptionCause.ERROR_PAY);
            }
            // 3、签名生成算法
            Long time = System.currentTimeMillis() / 1000;
            String timestamp = time.toString();
            returnMap.put("appId", CONST.appId);
            returnMap.put("partner_id", CONST.mch_id);
            returnMap.put("prepay_id", responseMap.get("prepay_id"));
            returnMap.put("nonce_str", responseMap.get("nonce_str"));
            returnMap.put("timestamp", timestamp);
            returnMap.put("package", "Sign=WXPay");
            returnMap.put("paySign", WXPayUtil.generateSignature(returnMap, CONST.key));//微信支付签名
            //return returnMap;
        } catch (Exception e) {
            throw new BizException(ConstantUtil.BizExceptionCause.ERROR_PAY);
        }

        String sign =WXPayUtil.generateSignature(returnMap, CONST.key);
        String money = String.valueOf(amount*100) ;

        String xml="<xml>"+

                "<appid>"+CONST.appId+"</appid>"+

                "<body><![CDATA["+detail+"]]></body>"+

                "<detail><![CDATA["+detail+"]]></detail>"+

                "<mch_id>"+ CONST.mch_id+"</mch_id>"+

                "<nonce_str>"+responseMap.get("nonce_str")+"</nonce_str>"+

                "<sign>"+sign+"</sign>"+

                "<notify_url>"+CONST.notify_url+"</notify_url>"+

                "<openid>"+openId+"</openid>"+

                "<out_trade_no>"+orderNo+"</out_trade_no>"+

                "<spbill_create_ip>"+ip+"</spbill_create_ip>"+

                "<total_fee>"+money+"</total_fee>"+

                "<trade_type>JSAPI</trade_type>"+

                "</xml>";

        String prepay_id="";

        try {

            prepay_id = ConstantUtil.httpRequest(CONST.pay_url,"POST",xml);

            if(prepay_id.equals("")){

                //错误提示

                System.out.println("统一支付接口获取预支付订单出错");

            }

        }catch (Exception e1) {

            e1.printStackTrace();

        }

        SortedMap<String, String> finalpackage =new TreeMap<String, String>();
        Long time = System.currentTimeMillis() / 1000;

        String timestamp =time.toString();

        String packages ="prepay_id="+prepay_id;

        finalpackage.put("appId", CONST.appId);

        finalpackage.put("nonceStr", responseMap.get("nonce_str"));

        finalpackage.put("package", packages);

        finalpackage.put("signType","MD5");

        finalpackage.put("timeStamp", timestamp);

        String finalsign =  WXPayUtil.generateSignature(finalpackage, CONST.key);


        Map<String,Object> map = new HashMap<>();
        map.put("nonceStr",responseMap.get("nonce_str"));
        map.put("packages",packages);
        map.put("paySign",finalsign);
        map.put("signType","MD5");
        map.put("timeStamp",timestamp);

        return map;
    }

    @Override
    public String notify(String notifyStr) throws Exception {
        return null;
    }
}