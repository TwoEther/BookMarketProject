package org.project.shop.repository;

import org.project.shop.domain.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CategoryRepository {
    public void save(Category category);

    public Category findByCategoryId(Long id);

    public Category findByCategoryName(String category1, String category2);

    public List<Category> findAllCategory();
}
