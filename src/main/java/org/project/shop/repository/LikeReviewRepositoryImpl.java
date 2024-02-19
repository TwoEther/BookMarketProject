package org.project.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.LikeReview;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.project.shop.domain.QLikeReview.likeReview;


@Repository
public class LikeReviewRepositoryImpl implements LikeReviewRepository{
    @PersistenceContext
    EntityManager em;

    private final JPAQueryFactory queryFactory;

    public LikeReviewRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void save(LikeReview likeReview) {
        em.persist(likeReview);
    }

    @Override
    public LikeReview findByMemberId(Long memberId) {
        return queryFactory.selectFrom(likeReview)
                .where(likeReview.member.id.eq(memberId))
                .fetchOne();
    }

    @Override
    public LikeReview findByReviewIdAndMemberId(Long reviewId, Long memberId) {
        return queryFactory.selectFrom(likeReview)
                .where(likeReview.review.id.eq(reviewId)
                        .and(likeReview.member.id.eq(memberId)))
                .fetchOne();
    }

    @Override
    public List<LikeReview> findAllLikeReview() {
        return queryFactory.selectFrom(likeReview)
                .fetch();
    }

    @Override
    public void deleteLikeReview(Long id) {
        queryFactory.delete(likeReview)
                .where(likeReview.id.eq(id))
                .execute();
    }
}
