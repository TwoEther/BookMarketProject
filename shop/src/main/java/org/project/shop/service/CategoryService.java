package org.project.shop.service;

import org.project.shop.domain.Category;

import java.util.List;

public interface CategoryService {
    public void save(Category category);

    public Category findByCategoryName(String category1, String category2);

    public List<String> findAllCategory2();
    public List<Category> findAllCategory();
}
