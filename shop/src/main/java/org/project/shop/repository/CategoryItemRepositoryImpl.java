package org.project.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.CategoryItem;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryItemRepositoryImpl implements CategoryItemRepository{
    @PersistenceContext
    EntityManager em;

    private final JPAQueryFactory queryFactory;

    public CategoryItemRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void save(CategoryItem categoryItem) {
        em.persist(categoryItem);
        em.flush();
    }
}
