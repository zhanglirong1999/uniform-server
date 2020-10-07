package com.school.uniform.service.impl;

import com.school.uniform.common.CONST;
import com.school.uniform.common.wxpay.*;
import com.school.uniform.exception.BizException;
import com.school.uniform.model.dao.mapper.PurchaseMapper;
import com.school.uniform.service.WxPayService;
import com.school.uniform.util.ConstantUtil;

import com.school.uniform.util.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class WxPayServiceImpl implements WxPayService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public Object unifiedOrder(String orderNo, double amount, String openId, HttpServletRequest request)
    {
        Map<String, String> requestMap = new HashMap<>();
        String money = String.valueOf(amount * 100);
        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //商品名称
        String body = "购买校服";
        //获取本机的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);
        try {
            IWxPayConfig iWxPayConfig = new IWxPayConfig();
            WXPay wxpay = new WXPay(iWxPayConfig);
            requestMap.put("body", body);                                     // 商品描述
            requestMap.put("appid", CONST.appId);
            requestMap.put("mch_id", CONST.mch_id);
            requestMap.put("out_trade_no", orderNo);                          // 商户订单号
            requestMap.put("total_fee", money);   // 总金额
            requestMap.put("spbill_create_ip", spbill_create_ip); // 终端IP
            requestMap.put("trade_type", "JSAPI");                              // App支付类型
            requestMap.put("notify_url", CONST.notify_url);   // 接收微信支付异步通知回调地址
            requestMap.put("openid", openId);
            System.out.println(requestMap);
            Map<String, String> resultMap = wxpay.unifiedOrder(requestMap);
            System.out.println(resultMap);
            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
//            String sign = WXPayUtil.generateSignature(resultMap, CONST.key);
//
//            String xml = "<xml>" +
//
//                    "<appid>" + CONST.appId + "</appid>" +
//
//                    "<body><![CDATA[" + body + "]]></body>" +
//
//                    "<detail><![CDATA[" + body + "]]></detail>" +
//
//                    "<mch_id>" + CONST.mch_id + "</mch_id>" +
//
//                    "<nonce_str>" + nonce_str + "</nonce_str>" +
//
//                    "<sign>" + sign + "</sign>" +
//
//                    "<notify_url>" + CONST.notify_url + "</notify_url>" +
//
//                    "<openid>" + openId + "</openid>" +
//
//                    "<out_trade_no>" + orderNo + "</out_trade_no>" +
//
//                    "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" +
//
//                    "<total_fee>" + money + "</total_fee>" +
//
//                    "<trade_type>JSAPI</trade_type>" +
//
//                    "</xml>";

            //可以直接生成xml
            String xml = WXPayUtil.generateSignedXml(resultMap,CONST.key, WXPayConstants.SignType.MD5);

            String result = "";

            try {
                result = ConstantUtil.httpRequest(CONST.pay_url, "POST", xml);
                Map map = WXPayUtil.xmlToMap(result);
                System.out.println("map:");
                System.out.println(map);
                String return_code = (String) map.get("return_code");//返回状态码

                //返回给移动端需要的参数
                Map<String, String> response = new HashMap<String, String>();
                if (return_code == "SUCCESS" || return_code.equals(return_code)) {
                    // 业务结果
                    response.put("appId", CONST.appId);
                    String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                    response.put("nonceStr", nonce_str);
                    response.put("package", "prepay_id="+prepay_id);
                    response.put("signType","MD5");
                    Long timeStamp = System.currentTimeMillis() / 1000;
                    response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误

                    String stringSignTemp = "appId=" + CONST.appId + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id + "&signType=" + CONST.SIGNTYPE + "&timeStamp=" + timeStamp;
                    //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                    String paySign = WXPayUtil.generateSignature(response, CONST.key);

                    response.put("paySign", paySign);
                    return response;
                    //更新订单信息
                    //业务逻辑代码
                }
            } catch (Exception e) {
                throw new BizException(ConstantUtil.BizExceptionCause.ERROR_PAY);
            }

        }catch (Exception e) {

            throw new BizException(ConstantUtil.BizExceptionCause.ERROR_PAY);

        }
        return null;
    }

    @Override
    public String callBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inputStream =  request.getInputStream();
        //获取请求输入流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len=inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);
        }
        outputStream.close();
        inputStream.close();
        // 预先设定返回的 response 类型为 xml
        response.setHeader("Content-type", "application/xml");
        // 读取参数，解析Xml为map
        Map<String, String> map = WXPayUtil.xmlToMap(new String(outputStream.toByteArray(),"utf-8"));
        // 转换为有序 map，判断签名是否正确
        boolean isSignSuccess = WXPayUtil.isSignatureValid(map,CONST.key);
        if (isSignSuccess) {
            // 签名校验成功，说明是微信服务器发出的数据
            String nonceStr = map.get("nonceStr");
            if (!purchaseMapper.getStateByNonce(nonceStr).equals("0")) // 判断该订单是否已经被接收处理过
            {
                return CONST.success();
            }
            // 可在此持久化微信传回的该 map 数据
            //..
            if (map.get("return_code").equals("SUCCESS")) {
                if (map.get("result_code").equals("SUCCESS")) {
                    purchaseMapper.updateState(nonceStr);  // 支付成功
                } else {
                    return CONST.fail();
                    // 支付失败
                }
            }
            return CONST.success();
        } else {
            // 签名校验失败（可能不是微信服务器发出的数据）
            return CONST.fail();
        }
    }


    }

