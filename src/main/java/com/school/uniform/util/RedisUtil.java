package com.school.uniform.util;

import com.school.uniform.common.CONST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    public void setSchoolIdInRedis(Long schoolId){
        redisTemplate.opsForValue().set(CONST.SCHOOL_ID,schoolId);
    }
    public Long getSchoolId(){
        System.out.println(redisTemplate.opsForValue().get(CONST.SCHOOL_ID));
        return (Long) redisTemplate.opsForValue().get(CONST.SCHOOL_ID);

    }

}
