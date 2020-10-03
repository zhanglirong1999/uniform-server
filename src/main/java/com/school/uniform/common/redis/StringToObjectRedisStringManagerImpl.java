package com.school.uniform.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Validated
@Component
public class StringToObjectRedisStringManagerImpl
        implements StringToObjectRedisStringManager {

    private ReentrantLock reentrantLock = new ReentrantLock(true);

    @Autowired
    private RedisTemplate<String, Object> myString2ObjectRedisTemplate;

    @Override
    public RedisTemplate<String, Object> getTemplate() {
        return this.myString2ObjectRedisTemplate;
    }

    @Override
    public Object getValue(@NotEmpty String key) {
        return myString2ObjectRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void setValue(String key, Object value) {
        myString2ObjectRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setValueWithTTL(
             String key, Object value,
             Long ttl,  TimeUnit timeUnit
    ) {
        myString2ObjectRedisTemplate.opsForValue().set(
                key, value, ttl, timeUnit
        );
    }

    @Override
    public Long incrementOrNew(
            @NotEmpty String key,
            @NotEmpty Long ttl,
            @NotEmpty TimeUnit timeUnit
    ) {
        if (!myString2ObjectRedisTemplate.hasKey(key)) {
            setValueWithTTL(key, 1L, ttl, timeUnit);
        } else {
            myString2ObjectRedisTemplate.opsForValue().increment(key, 1L);
        }
        Integer t = (Integer) getValue(key);
        return Long.parseLong(String.valueOf(t));
    }
}

