package org.project.shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.shop.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class CategoryServiceTest {
    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @DisplayName("서로 다른 카테고리 중복 테스트")
    @Test
    public void diffCategoryTest() {
        Category category1 = new Category("국내 서적", "소설");
        Category category2 = new Category("국내 서적", "에세이");

        categoryServiceImpl.save(category1);
        categoryServiceImpl.save(category2);

        Category findCategory = categoryServiceImpl.findByCategoryName("국내 서적", "소설");
        assertThat(findCategory.getCategory1()).isEqualTo("국내 서적");

        List<Category> findAllCategory = categoryServiceImpl.findAllCategory();
        assertThat(findAllCategory.size()).isEqualTo(2);
    }
}
