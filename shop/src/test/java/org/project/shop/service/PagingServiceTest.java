package org.project.shop.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.project.shop.custom.CustomPageRequest;
import org.project.shop.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PagingServiceTest {
    @Autowired
    private ItemServiceImpl itemServiceImpl;
    @Test
    public void pagingTest() {
        //given
        int page = 50;
        int size = 6;

        
        // 50명의 Member 가입
        for (int i = 0; i < page; i++) {
            Item item = Item.builder().name("name"+i).price(10000).stockQuantity(30).
                        build();
            itemServiceImpl.saveItemNoImage(item);
        }
        for (int i = 0; i < Math.floorDiv(page, size)+1; i++) {
            PageRequest pageRequest = PageRequest.of(i, size);
            Page<Item> allItem = itemServiceImpl.findAllItem(pageRequest);

            assertThat(allItem.getTotalElements()).isEqualTo(6);
        }
    }
}
