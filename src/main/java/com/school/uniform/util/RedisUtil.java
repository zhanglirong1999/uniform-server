package com.school.uniform.util;

import com.school.uniform.common.CONST;
import com.school.uniform.model.dao.mapper.SolicitationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SolicitationMapper solicitationMapper;

    public void setSchoolIdInRedis(Long schoolId,String accountId){
//        redisTemplate.opsForValue().set(CONST.SCHOOL_ID,schoolId);
        redisTemplate.opsForHash().put(accountId, CONST.SCHOOL_ID,schoolId);

    }
    public Long getSchoolId(String accountId){
        System.out.println(redisTemplate.opsForHash().get(accountId,CONST.SCHOOL_ID));
        return (Long) redisTemplate.opsForHash().get(accountId,CONST.SCHOOL_ID);
    }

    public void setSolicitId(Long sid,String accountId){
//        redisTemplate.opsForValue().set(CONST.SOLICIT_ID,sid);
        redisTemplate.opsForHash().put(accountId, CONST.SOLICIT_ID,sid);
        //solicit中还要查询schoolId，并且存入redis
        Long schoolId = solicitationMapper.getSchoolId(sid);
        redisTemplate.opsForHash().put(accountId, CONST.SCHOOL_ID,schoolId);


    }
    public Long getSolicitId(String accountId){
        return (Long) redisTemplate.opsForHash().get(accountId,CONST.SOLICIT_ID);
    }

}
