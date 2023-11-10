package org.project.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.QReview;
import org.project.shop.domain.Review;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.project.shop.domain.QReview.review;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository{
    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void save(Review review) {
        em.persist(review);
    }

    @Override
    public Review findOneReview(Long reviewId) {
        return queryFactory.selectFrom(review)
                .where(review.id.eq(reviewId))
                .fetchOne();
    }

    @Override
    public List<Review> findAllReviewByItemId(Long itemId) {
        return queryFactory.selectFrom(review)
                .where(review.item.id.eq(itemId))
                .fetch();
    }

    @Override
    public List<Review> findAllReviewByMemberId(Long memberId) {
        return queryFactory.selectFrom(review)
                .where(review.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<Review> findAllReview() {
        return queryFactory.selectFrom(review)
                .fetch();
    }
}
