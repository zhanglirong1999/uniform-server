package com.school.uniform.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.school.uniform.common.CONST;
import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.exception.BizException;
import com.school.uniform.model.dao.entity.Account;
import com.school.uniform.model.dao.entity.Student;
import com.school.uniform.model.dao.mapper.AccountMapper;
import com.school.uniform.model.dao.mapper.StudentMapper;
import com.school.uniform.model.dto.post.PhoneUsernameCodeVerifier;
import com.school.uniform.model.dto.post.PhoneUsernameVerifier;
import com.school.uniform.model.dto.post.Register;
import com.school.uniform.service.AccountService;
import com.school.uniform.service.VerifyService;
import com.school.uniform.util.ConstantUtil;
import com.school.uniform.util.TokenUtil;
import com.school.uniform.util.WeChatUtil;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@RestController
@CrossOrigin(origins = "http://localhost:18080", maxAge = 7200)
@RequestMapping("/account")
@WebResponse
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private StudentMapper studentMapper;


    String access_token = "";
    long expireTime = 0L;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private HttpServletRequest request;

    String getAccessToken(String openid) {
        if (System.currentTimeMillis() > expireTime) {
            // 使用appid和secret访问接口.获取公众号的access_token
            String wxApiUrl = "https://api.weixin.qq.com/cgi-bin/token?" +
                    "openid=" + openid +
                    "&secret=" + CONST.appSecret +
                    "&grant_type=client_credential";
            String respronse = restTemplate.getForObject(wxApiUrl, String.class);
            System.out.println("执行了");
        }
        return access_token;
    }

    /**
     * 登录
     * @param js_code
     * @return
     */
    @PostMapping("/login")
    public Object getLogin(
            @RequestParam String js_code
    ){
        // 微信登陆，获取openid
        String wxApiUrl = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + CONST.appId +
                "&secret=" + CONST.appSecret +
                "&js_code=" + js_code +
                "&grant_type=authorization_code";
        String respronse = restTemplate.getForObject(wxApiUrl, String.class);
        Map res = new Gson().fromJson(respronse, Map.class);
        System.out.println(res);
//        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + CONST.appId + "&secret=" + CONST.appSecret + "&js_code=" + js_code + "&grant_type=authorization_code";
//        String str = WeChatUtil.httpRequest(url, "GET", null);
//        if (StringUtils.isEmpty(str)) {
//            return null;
//        } else {
//            return JSONObject.parseObject(str);
//        }
        String openid = (String) res.get("openid");
        if (openid != null && !openid.equals("")) {
            isRegister register = new isRegister();
            Account account = accountMapper.selectOneByExample(
                    Example.builder(Account.class).where(Sqls.custom().andEqualTo("openId",openid))
                    .build()
            );
            if(account != null){
                String accountId = account.getAccountId();
                register.setIsRegister(true);
                register.setToken(TokenUtil.createToken(accountId));
                return register;
            }else {
                //需要新注册
                Account newAccount = new Account();
                newAccount.setOpenId(openid);
                newAccount.setAccountId(UUID.randomUUID().toString());
                newAccount.setLastTime(new Date());
                accountMapper.insertSelective(newAccount);
                register.setToken(newAccount.getAccountId());
                register.setIsRegister(false);
                return register;
            }
        }else {
            throw new BizException(ConstantUtil.BizExceptionCause.NOT_OPENID);
        }
    }

    @Data
    class isRegister{
        Boolean isRegister;
        String token;
    }

    /**
     * 注册
     * @param register
     * @return
     */
    @TokenRequired
    @PostMapping("/register")
    public Object test(
            @RequestBody Register register
            ){
        //获取accountId
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        System.out.println(accountId);
        accountService.registerUser(register,accountId);
        return "success";
    }

    @PostMapping("/getToken")
    public Object getToken(){
//        String accountId= UUID.randomUUID().toString();
        String accountId ="74e22097-00ff-489f-9a90-0a318558b34a";
        String token = TokenUtil.createToken(accountId);
        System.out.println(token);
        return token;
    }

    /**
     * 获取用户信息
     * @return
     */
    @TokenRequired
    @GetMapping("/info")
    public Object getUserInfo(){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        return accountService.getUserInfo(accountId);
    }

    /**
     * 用户拥有学生信息（以学校为前提）
     * @return
     */
    @TokenRequired
    @GetMapping("/studentList")
    public Object getStudentList(

    ){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        return accountService.getStudentList(accountId);
    }



}
