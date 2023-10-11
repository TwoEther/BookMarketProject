package org.project.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.Category;
import org.project.shop.domain.QCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.project.shop.domain.QCategory.category;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository{
    @PersistenceContext
    EntityManager em;

    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void save(Category category) {
        em.persist(category);
    }

    @Override
    public Category findByCategoryId(Long id) {
        return queryFactory.selectFrom(category)
                .where(category.id.eq(id))
                .fetchOne();
    }

    @Override
    public Category findByCategoryName(String category1, String category2) {
        return queryFactory.selectFrom(category)
                .where(category.category1.like(category1)
                        .and(category.category2.like(category2))
                ).fetchOne();
    }

    @Override
    public List<Category> findAllCategory() {
        return queryFactory.selectFrom(category)
                .fetch();
    }


}
