package org.project.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.LikeItem;
import org.project.shop.domain.QLikeItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.project.shop.domain.QLikeItem.*;

@Repository
public class LikeItemRepositoryImpl implements LikeItemRepository{
    @PersistenceContext
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public LikeItemRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void save(LikeItem likeItem) {
        em.persist(likeItem);
    }

    @Override
    public LikeItem findLikeItemByMemberId(String userId) {
        return queryFactory.selectFrom(likeItem)
                .where(likeItem.member.userId.eq(userId))
                .fetchOne();
    }

    @Override
    public List<LikeItem> findAllLikeItem() {
        return queryFactory.selectFrom(likeItem)
                .fetch();
    }
}
