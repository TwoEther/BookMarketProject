package org.project.shop.repository;

import org.project.shop.domain.CategoryItem;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryItemRepository {
    public void save(CategoryItem categoryItem);
}