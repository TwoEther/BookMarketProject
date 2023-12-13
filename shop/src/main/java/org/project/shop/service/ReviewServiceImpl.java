package org.project.shop.service;

import lombok.RequiredArgsConstructor;
import org.project.shop.domain.Review;
import org.project.shop.repository.ReviewRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepositoryImpl reviewRepositoryImpl;
    @Override
    @Transactional
    public void save(Review review) {
        reviewRepositoryImpl.save(review);
    }

    @Override
    public Review findOneReview(Long reviewId) {
        return reviewRepositoryImpl.findOneReview(reviewId);
    }

    @Override
    public List<Review> findAllReviewByItemId(Long ItemId) {
        return reviewRepositoryImpl.findAllReviewByItemId(ItemId);
    }

    @Override
    public List<Review> findAllReviewByMemberId(Long memberId) {
        return reviewRepositoryImpl.findAllReviewByMemberId(memberId);
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewRepositoryImpl.deleteReview(reviewId);
    }

    @Override
    public List<Review> findAllReview() {
        return reviewRepositoryImpl.findAllReview();
    }
}
