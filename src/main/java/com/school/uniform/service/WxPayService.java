package com.school.uniform.service;

import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Validated
public interface WxPayService {

    /**
     * @Description: 微信支付统一下单
     * @param orderNo: 订单编号
     * @param amount: 实际支付金额
     * @return
     */
    Object unifiedOrder(String orderNo, int amount, String openId,  HttpServletRequest request) throws Exception;

    /**
     * @Description: 订单支付异步通知
     * @param : 微信异步通知消息字符串
     * @return
     */
    String callBack(HttpServletRequest request, HttpServletResponse response) throws Exception;

//    /**
//     * @Description: 退款
//     * @param orderNo: 订单编号
//     * @param amount: 实际支付金额
//     * @param refundReason: 退款原因
//     * @return
//     */
//    Object refund(String orderNo, double amount, String refundReason) throws Exception;

}