package org.project.shop.service;

import org.project.shop.domain.CategoryItem;

public interface CategoryItemService {
    public void save(CategoryItem categoryItem);

    public CategoryItem findByCategory(String category1, String category2);
}
