package org.project.shop.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.project.shop.config.QuerydslConfig;
import org.project.shop.domain.Category;
import org.project.shop.domain.QCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.project.shop.domain.Item;

import java.util.List;

import static org.project.shop.domain.QCategory.category;
import static org.project.shop.domain.QItem.item;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository{
    @PersistenceContext
    EntityManager em;

    private final JPAQueryFactory queryFactory;


    @Override
    public void save(Item item) {
        em.persist(item);
    }

    @Override
    public void clear() {
        queryFactory.delete(item)
                .execute();
    }

    @Override
    public Item findOneItem(Long id){
        return queryFactory.select(item)
                .from(item)
                .where(item.id.eq(id))
                .fetchOne();
    }

    @Override
    public Page<Item> findAllItem(PageRequest pageRequest){
        List<Item> result = queryFactory.select(item)
                .from(item)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();
        return new PageImpl<>(result);
    }

    @Override
    public List<Item> findByItemWithCategory(String category2) {
        return queryFactory.selectFrom(item)
                .where(item.category.category2.eq(category2))
                .limit(3)
                .fetch();
    }

    @Override
    public int getAllItemNum() {
        List<Item> allItem = queryFactory.selectFrom(item)
                .fetch();
        return allItem.size();
    }

    @Override
    public Page<Item> findByKeyword(PageRequest pageRequest, String keyword) {
        List<Item> result = queryFactory.selectFrom(item)
                .where(item.name.like("%" + keyword + "%").or(
                        item.author.like("%" + keyword + "%").or(
                                item.category.category1.like("%" + keyword + "%").or(
                                        item.category.category2.like("%" + keyword + "%")
                                ))
                        )
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();
        return new PageImpl<>(result);
    }

    @Override
    public List<Item> orderByCategory() {
        return queryFactory.selectFrom(item)
                .orderBy(item.category.category2.asc())
                .fetch();

    }

    @Override
    public void deleteByItemId(Long itemId) {
        queryFactory.delete(item).
                where(item.id.eq(itemId))
                .execute();
    }
}
