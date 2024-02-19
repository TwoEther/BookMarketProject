package org.project.shop.email;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.shop.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EmailServiceTest {
    @Autowired
    private RedisService redisService;

    @Test
    public void redisTest() {
        String email = "test@test.com";
        String authCode = "123dwe";

//        redisService.setRedisTemplate(email, authCode, Duration.ofMillis(2300));
    }
}
