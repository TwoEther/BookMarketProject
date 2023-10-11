package org.project.shop.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.shop.domain.Category;
import org.project.shop.domain.CategoryItem;
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

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @DisplayName("카테고리 설정 테스트")
    @Test
    public void setItemCategoryTest() {
        Item item1 = new Item("item1", 30000, 5);
        Item item2 = new Item("item2", 50000, 3);

        String category1 = "국내 서적";
        String category2 = "소설";
        String category3 = "인문";

        Category category = categoryServiceImpl.findByCategoryName(category1, category2);
        if (category == null) {
            Category newCategory = new Category(category1, category2);
            categoryServiceImpl.save(newCategory);
        }

        Category findCategory = categoryServiceImpl.findByCategoryName(category1, category2);

        CategoryItem categoryItem = new CategoryItem();
        categoryItem.setCategory(findCategory);
        categoryItem.setItem(item1);

        System.out.println("categoryItem.toString() = " + categoryItem.toString());




    }
}
