package org.project.shop.repository;

import org.project.shop.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    public void save(Review review);

    public Review findOneReview(Long reviewId);

    public List<Review> findAllReviewByItemId(Long itemId);

    public Page<Review> findPageReviewByItemId(PageRequest pageRequest, Long itemId);
    public List<Review> findAllReviewByMemberId(Long memberId);

    public Optional<Review> findOneReviewByItemIdAndMemberId(Long itemId, Long memberId);
    public List<Review> findAllReview();

    public void deleteAll();

    public void deleteReview(Long reviewId);
}
