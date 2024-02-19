package org.project.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Transactional
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public String getRedisTemplateValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRedisTemplateValue(String key) {
        redisTemplate.delete(key);
    }

    public void setRedisTemplate(String key, String value, Duration duration) {
        if (getRedisTemplateValue(key) != null) {
            deleteRedisTemplateValue(key);
        }

        redisTemplate.opsForValue().set(key, value, duration);
    }
}
