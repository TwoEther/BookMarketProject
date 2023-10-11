package org.project.shop.service;

import org.project.shop.domain.CategoryItem;
import org.project.shop.repository.CategoryItemRepositoryImpl;

public class CategoryItemImpl implements CategoryItemService{
    private final CategoryItemRepositoryImpl categoryItemRepositoryImpl;

    public CategoryItemImpl(CategoryItemRepositoryImpl categoryItemRepositoryImpl) {
        this.categoryItemRepositoryImpl = categoryItemRepositoryImpl;
    }

    @Override
    public void save(CategoryItem categoryItem) {
        categoryItemRepositoryImpl.save(categoryItem);
    }

    @Override
    public CategoryItem findByCategory(String category1, String category2) {
        return null;
    }
}
