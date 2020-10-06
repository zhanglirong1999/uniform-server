package com.school.uniform.controller;

import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.model.dto.post.Payment;
import com.school.uniform.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/wxpay")
@WebResponse
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;

//    @TokenRequired
//    @PostMapping("/payment")
//    public Object payment(
//            @RequestBody Payment payment){
//        return wxPayService.unifiedOrder()
//    }
//
//    @TokenRequired
//    @PostMapping("/callback")
//    public Object callBack(){
//
//    }

}
