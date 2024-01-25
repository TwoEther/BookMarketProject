package org.project.shop.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

@SpringBootTest
public class RedisServiceTest {
    @Autowired
    private RedisService redisService;

    @DisplayName("Redis 연동 테스트")
    @Test
    public void redisTest() {
        String key = "testKey";
        String data = "testData";

        redisService.setRedisTemplate(key, data, Duration.ofMillis(3000));

        String expValue = redisService.getRedisTemplateValue("testKey");
        Assertions.assertThat(expValue).isEqualTo(data);
    }

}
