package com.school.uniform.controller;

import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.model.dto.post.PhoneUsernameCodeVerifier;
import com.school.uniform.model.dto.post.PhoneUsernameVerifier;
import com.school.uniform.service.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin(origins = "http://localhost:18080", maxAge = 7200)
@RequestMapping("/admin")
@WebResponse
public class AdminController {
    @Autowired
    private VerifyService verifyService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 管理员登录
     * @param phoneUsernameCodeVerifier
     * @return
     */
    @PostMapping("/login")
    public Object adminLogin(
            @RequestBody PhoneUsernameCodeVerifier phoneUsernameCodeVerifier
    ) throws NoSuchAlgorithmException {
        return verifyService.postLogin(request,phoneUsernameCodeVerifier);
//        return 1;
    }

    /**
     * 管理员
     * 获取手机验证码
     * @param phoneUsernameVerifier
     * @return
     * @throws Exception
     */
    @PostMapping("/code")
    public Object getAdminCode(
            @RequestBody PhoneUsernameVerifier phoneUsernameVerifier
    ) throws Exception {
        verifyService.postVerifyCode(request,phoneUsernameVerifier);
        return "验证码已发送!";
    }
}
