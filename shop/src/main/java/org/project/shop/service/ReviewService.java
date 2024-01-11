package org.project.shop.service;

import org.project.shop.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    public void save(Review review);

    public Review findOneReview(Long reviewId);

    public List<Review> findAllReviewByItemId(Long ItemId);

    public Page<Review> findPageReviewByItemId(PageRequest pageRequest, Long itemId);

    public List<Review> findAllReviewByMemberId(Long memberId);

    public void deleteReview(Long reviewId);

    public List<Review> findAllReview();
}
