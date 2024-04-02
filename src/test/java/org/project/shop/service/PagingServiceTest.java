package org.project.shop.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.shop.custom.CustomPageRequest;
import org.project.shop.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.project.shop.domain.QItem.item;

@ActiveProfiles("test")
@SpringBootTest
public class PagingServiceTest {
    @Autowired
    private ItemServiceImpl itemServiceImpl;
    @Autowired
    EntityManager em;
    @Autowired
    private JPAQueryFactory queryFactory;

    @Test
    @DisplayName("페이징 테스트")
    public void previousPageTest() {
        for (int page = 0; page < 8; page++) {
            PageRequest pageRequest = PageRequest.of(page, 6);
            Page<Item> pageItems = itemServiceImpl.findAllItem(pageRequest);
            System.out.println("pageItems.hasPrevious() = " + pageItems.hasPrevious());
            System.out.println("pageItems.hasNext() = " + pageItems.hasNext());
            System.out.println("pageItems.getContent() = " + pageItems.getContent());
        }
    }
    @Test
    @DisplayName("페이징 함수 동작원리 테스트")
    public void pagingStructureTest() {
    }


}
