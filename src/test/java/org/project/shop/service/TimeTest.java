package org.project.shop.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class TimeTest {
    @Test
    public void orderNumberCreateTest() {
        // 주문번호 생성 알고리즘
        // 년,월,일,시,분,초 + 초 소수점 5자리
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSSSS"));
        StringBuilder time = new StringBuilder();
        for (String s : data.split("-")) time.append(s);

        System.out.println("time = " + time);
    }
    
       
}
