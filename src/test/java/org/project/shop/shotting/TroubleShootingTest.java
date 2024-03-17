package org.project.shop.shotting;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.shop.domain.Item;
import org.project.shop.service.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
public class TroubleShootingTest {
    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @BeforeEach
    @Transactional
    public void setUp() {
        int batch_size = 30000;
        for (int i = 0; i < batch_size; i++) {
            Item item = Item.builder().
                    name("book"+i).
                    price(30000*(i%5)).
                    stockQuantity(3).
                    build();
            itemServiceImpl.saveItemNoImage(item);
        }
    }


    @Test
    @DisplayName("3만건에 대해서 쿼리 실행시간 테스트")
    public void shootingTest() {
        /*
            쿼리 실행시간 테스트
        */
        List<Item> allItem = itemServiceImpl.findAllItem();
        Assertions.assertThat(allItem.size()).isEqualTo(30000);
    }

}
