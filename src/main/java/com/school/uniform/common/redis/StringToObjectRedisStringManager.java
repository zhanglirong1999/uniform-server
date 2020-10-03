package com.school.uniform.common.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.concurrent.TimeUnit;

@Validated
public interface StringToObjectRedisStringManager {

    RedisTemplate<String, Object> getTemplate();

    Object getValue(@NotEmpty String key);

    void setValue(@NotEmpty String key, @NotEmpty Object value);

    void setValueWithTTL(
            String key, Object value,
             Long ttl,  TimeUnit timeUnit
    );

    // 线程安全
    Long incrementOrNew(
            @NotEmpty String key,
            @NotEmpty Long ttl,
            @NotEmpty TimeUnit timeUnit
    );
}

