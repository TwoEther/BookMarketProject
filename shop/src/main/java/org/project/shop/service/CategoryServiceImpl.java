package org.project.shop.service;

import org.project.shop.domain.Category;
import org.project.shop.repository.CategoryRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepositoryImpl categoryRepositoryImpl;

    public CategoryServiceImpl(CategoryRepositoryImpl categoryRepositoryImpl) {
        this.categoryRepositoryImpl = categoryRepositoryImpl;
    }


    @Override
    public void save(Category category) {
        categoryRepositoryImpl.save(category);
    }

    @Override
    public Category findByCategoryName(String category1, String category2) {
        return categoryRepositoryImpl.findByCategoryName(category1, category2);
    }

    @Override
    public List<String> findAllCategory2() {
        return categoryRepositoryImpl.findAllCategory2();
    }
    @Override
    public List<Category> findAllCategory() {
        return categoryRepositoryImpl.findAllCategory();
    }
}
