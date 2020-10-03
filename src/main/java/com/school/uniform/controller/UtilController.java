package com.school.uniform.controller;

import com.school.uniform.common.annotation.WebResponse;
import com.school.uniform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@WebResponse
public class UtilController {
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/scene")
    public Object getSchool(
    ){
        Long schoolId =2943904830L;
        redisUtil.setSchoolIdInRedis(schoolId);
        return schoolId;
    }

}
