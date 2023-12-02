package org.project.shop.service;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ItemServiceTest {
    @Autowired
    private MemberServiceImpl memberServiceImpl;

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @Autowired
    private ItemRepositoryImpl itemRepositoryImpl;

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    /*
    @DisplayName("카테고리 설정 테스트")
    @Test
    public void setItemCategoryTest() {
        Item item1 = new Item("item1", 30000, 5);
        Item item2 = new Item("item2", 50000, 3);
        String category1 = "국내 서적";
        String category2 = "소설";
        String category3 = "인문";

        Category categoryObj1 = categoryServiceImpl.findByCategoryName(category1, category2);
        if (categoryObj1 == null) {
            Category newCategory = new Category(category1, category2);
            System.out.println("newCategory.toString() = " + newCategory.toString());
            categoryServiceImpl.save(newCategory);
        }

        Category categoryObj2 = categoryServiceImpl.findByCategoryName(category1, category3);
        if (categoryObj2 == null) {
            Category newCategory = new Category(category1, category3);
            categoryServiceImpl.save(newCategory);
        }
        List<Category> findAllCategory = categoryServiceImpl.findAllCategory();
        for (Category category : findAllCategory) {
            System.out.println("category.toString() = " + category.toString());
        }

        Category findCategory1 = categoryServiceImpl.findByCategoryName(category1, category2);
        Category findCategory2 = categoryServiceImpl.findByCategoryName(category1, category3);

    }

    @DisplayName("카테고리로 아이템 찾기")
    @Test
    public void findByItemWithCategory() {
        for (int i = 0; i < 5; i++) {
            String name = "item" + i;
            int price = (int) (Math.random() * 30000) + 10000;
            int stockQuantity = (int) (Math.random() * 100);

            Item item = new Item(name, price, stockQuantity);
            itemRepositoryImpl.save(item);
        }

        List<String> countries = new ArrayList<>();
        List<String> categories = new ArrayList<>();

        countries.add("국내 서적");
        countries.add("외국 서적");

        categories.add("사회/정치");
        categories.add("경영/경제");

        Category category1 = new Category(countries.get(0), categories.get(0));
        Category category2 = new Category(countries.get(0), categories.get(1));
        Category category3 = new Category(countries.get(1), categories.get(0));
        Category category4 = new Category(countries.get(1), categories.get(1));

        categoryServiceImpl.save(category1);
        categoryServiceImpl.save(category2);
        categoryServiceImpl.save(category3);
        categoryServiceImpl.save(category4);

        List<Item> findAllitem = itemServiceImpl.findAllItem();
        Item item1 = findAllitem.get(0);
        item1.setCategory(category1);
        Category findCategory = item1.getCategory();
        assertThat(findCategory.getCategory1()).isEqualTo(category1.getCategory1());
    }
    */
    @BeforeEach
    public void createItems() {
        for (int i = 0; i < 5; i++) {
            String name = "item" + i;
            int price = (int) (Math.random() * 30000) + 10000;
            int stockQuantity = (int) (Math.random() * 100);

            Item item = new Item(name, price, stockQuantity);
            itemRepositoryImpl.save(item);
        }
    }
    @BeforeEach
    public void createCategories() {
        List<String> countries = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        countries.add("국내 서적");
        countries.add("외국 서적");

        categories.add("사회/정치");
        categories.add("경영/경제");
        categories.add("과학");

        for (int i = 0; i < countries.size(); i++) {
            String country = countries.get(i);
            for (int j = 0; j < categories.size(); j++) {
                String category = categories.get(j);
                Category newCategory = new Category(country, category);
                categoryServiceImpl.save(newCategory);
            }
        }
    }
    
    @DisplayName("GroupByCategory 테스트")
    @Test
    public void groupByCategoryTest() {
        PageRequest pageRequest = CustomPageRequest.customPageRequest();
        List<Item> savedItems = (List<Item>) itemServiceImpl.findAllItem(pageRequest);
        List<Category> savedCategory = categoryServiceImpl.findAllCategory();

        for (int i = 0; i < savedItems.size(); i++) {
            savedItems.get(i).setCategory(savedCategory.get(i));
        }

        for (Item savedItem : savedItems) {
            System.out.println("savedItem.toString() = " + savedItem.toString());
        }

        List<Item> findAllItems = itemServiceImpl.orderByCategory();
        for (Item findAllItem : findAllItems) {
            System.out.println("findAllItem.toString() = " + findAllItem.toString());
        }
    } 
}
