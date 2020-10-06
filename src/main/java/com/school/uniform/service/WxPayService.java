package com.school.uniform.service;

import org.springframework.validation.annotation.Validated;

@Validated
public interface WxPayService {

    /**
     * @Description: 微信支付统一下单
     * @param orderNo: 订单编号
     * @param amount: 实际支付金额
     * @return
     */
    Object unifiedOrder(String orderNo, double amount,String openId,String ip) throws Exception;

    /**
     * @Description: 订单支付异步通知
     * @param notifyStr: 微信异步通知消息字符串
     * @return
     */
    String notify(String notifyStr) throws Exception;

//    /**
//     * @Description: 退款
//     * @param orderNo: 订单编号
//     * @param amount: 实际支付金额
//     * @param refundReason: 退款原因
//     * @return
//     */
//    Object refund(String orderNo, double amount, String refundReason) throws Exception;

}