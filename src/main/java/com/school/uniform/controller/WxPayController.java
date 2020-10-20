package com.school.uniform.controller;

import com.school.uniform.common.CONST;
import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.model.dao.mapper.AccountMapper;
import com.school.uniform.model.dto.post.Payment;
import com.school.uniform.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:18080", maxAge = 7200)
@RequestMapping("/wxpay")
@WebResponse
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AccountMapper accountMapper;
    @TokenRequired
    @PostMapping("/payment")
    public Object payment(
            @RequestBody Payment payment) throws Exception {
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        String openId = accountMapper.getOpenId(accountId);
        return wxPayService.unifiedOrder(String.valueOf(payment.getOrderId()) ,payment.getPrice(),openId,request);
    }

    @TokenRequired
    @PostMapping("/callback")
    public Object callBack(HttpServletRequest request,HttpServletResponse response) throws Exception {
        System.out.println("开始回调");
        return wxPayService.callBack(request,response);
    }

}
