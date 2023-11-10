package org.project.shop.repository;

import org.project.shop.domain.Review;

import java.util.List;

public interface ReviewRepository {
    public void save(Review review);

    public Review findOneReview(Long reviewId);

    public List<Review> findAllReviewByItemId(Long itemId);
    public List<Review> findAllReviewByMemberId(Long memberId);
    public List<Review> findAllReview();
}
