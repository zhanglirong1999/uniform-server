package com.school.uniform.controller;

import com.alibaba.fastjson.JSONObject;
import com.school.uniform.common.CONST;
import com.school.uniform.common.annotation.TokenRequired;
import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.exception.BizException;
import com.school.uniform.service.GetWxAccessCodeTask;
import com.school.uniform.service.ProductService;
import com.school.uniform.service.QCloudFileManager;
import com.school.uniform.service.SchoolService;
import com.school.uniform.util.ConstantUtil;
import com.school.uniform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@WebResponse
public class UtilController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private GetWxAccessCodeTask wxAccessCodeTask;
    @Autowired
    private QCloudFileManager qCloudFileManager;

    @Autowired
    private ProductService productService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SchoolService schoolService;

    @TokenRequired
    @GetMapping("/scene")
    public Object getSchool(
            @RequestParam Long sid,
            @RequestParam String flag
    ){
        String accountId = (String) request.getAttribute(CONST.ACL_ACCOUNTID);
        if(flag.equals("0")) {
            Long schoolId = sid;
            redisUtil.setSchoolIdInRedis(schoolId,accountId);
            System.out.println(redisUtil.getSchoolId(accountId));
        }else if (flag.equals("1")){
            Long solicitId = sid;
            redisUtil.setSolicitId(solicitId,accountId);
            System.out.println(redisUtil.getSolicitId(accountId));
        }
        Long schoolIds = redisUtil.getSchoolId(accountId);    //无论是通过学校id还是征订id都需要把学校id存在redis中

        return schoolService.getUserStudent(schoolIds,accountId);
    }


    @PostMapping("/getCode")
    public Object getAccessToken(
            @RequestParam Long sid,
            @RequestParam String flag
    ) throws Exception {
        String fileName = UUID.randomUUID().toString() + System.currentTimeMillis()+ (".jpg");
        StringBuffer backUrl = new StringBuffer(); // 回调url
        StringBuffer info = new StringBuffer("https://api.weixin.qq.com/wxa/getwxacodeunlimit?");
        String scene;
        if(flag.equals("0")) {
            scene = "schoolId:"+sid;
        }else if(flag.equals("1")){
            scene = "sid" + sid;
        }else {
            throw new BizException(ConstantUtil.BizExceptionCause.ERROR_STATE);
        }
        String accessToken = wxAccessCodeTask.accessCode();
        URL url = null;
        url = new URL(info.append("access_token=").append(accessToken).toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
        JSONObject paramJson = new JSONObject();
        paramJson.put("scene", scene);

        printWriter.write(paramJson.toString());
        // flush输出流的缓冲
        printWriter.flush();
        //开始获取数据
        BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());

        MultipartFile multipartFile = new MockMultipartFile(fileName,fileName,"", bis);

        return productService.uploadFile(multipartFile);  //生成二维码的url
    }



}
