package org.project.shop.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    public void previousPageTest() {
        for (int page = 0; page < 8; page++) {
            PageRequest pageRequest = PageRequest.of(page, 6);
            Page<Item> pageItems = itemServiceImpl.findAllItem(pageRequest);
            System.out.println("pageItems.hasPrevious() = " + pageItems.hasPrevious());
            System.out.println("pageItems.hasNext() = " + pageItems.hasNext());
            System.out.println("pageItems.getContent() = " + pageItems.getContent());
        }
    }


}
