package org.project.shop.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.shop.custom.CustomPageRequest;
import org.project.shop.domain.Category;
import org.project.shop.domain.Item;
import org.project.shop.repository.ItemRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ItemServiceTest {
    @Autowired
    private MemberServiceImpl memberServiceImpl;

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @BeforeEach
    public void setUp() {
        List<String> countries = new ArrayList<>();
        List<String> categories = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String name = "item" + i;
            int price = (int) (Math.random() * 30000) + 10000;
            int stockQuantity = (int) (Math.random() * 100);

            Item item = new Item(name, price, stockQuantity);
            itemServiceImpl.saveItemNoImage(item);
        }

        countries.add("국내 서적");
        countries.add("외국 서적");

        categories.add("사회/정치");
        categories.add("경영/경제");
        categories.add("과학");

        for (String country : countries) {
            categories.forEach(category -> {
                categoryServiceImpl.save(new Category(country, category));
            });
        }
    }

    @AfterEach
    public void cleanUp() {
        itemServiceImpl.deleteAll();
        categoryServiceImpl.deleteAll();
    }


    @DisplayName("GroupByCategory 테스트")
    @Test
    public void groupByCategoryTest() {
        List<Item> savedItems = itemServiceImpl.findAllItem();
        List<Category> savedCategory = categoryServiceImpl.findAllCategory();
        List<Item> findAllItems = itemServiceImpl.orderByCategory();
    }


}
