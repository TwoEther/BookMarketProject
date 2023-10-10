package org.project.shop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.shop.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ItemServiceTest {
    @Autowired
    private MemberServiceImpl memberServiceImpl;

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @DisplayName("카테고리 설정 테스트")
    @Test
    public void setItemCategoryTest() {
        /*
            1. Item에서 Category에 대한 정보가 들어올때 CategoryItem에서
               Category에 대한 정보를 찾을수 있어야 함
         */
    }
}
