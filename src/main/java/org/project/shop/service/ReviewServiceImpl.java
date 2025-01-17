package org.project.shop.service;

import lombok.RequiredArgsConstructor;
import org.project.shop.domain.Review;
import org.project.shop.repository.ReviewRepositoryImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

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
    public Page<Review> findPageReviewByItemId(PageRequest pageRequest, Long itemId) {
        return reviewRepositoryImpl.findPageReviewByItemId(pageRequest, itemId);
    }

    @Override
    public List<Review> findAllReviewByMemberId(Long memberId) {
        return reviewRepositoryImpl.findAllReviewByMemberId(memberId);
    }

    @Override
    public Optional<Review> findOneReviewByItemIdAndMemberId(Long itemId, Long memberId) {
        return reviewRepositoryImpl.findOneReviewByItemIdAndMemberId(itemId, memberId);
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewRepositoryImpl.deleteReview(reviewId);
    }

    @Override
    public void deleteAll() {
        reviewRepositoryImpl.deleteAll();
    }

    @Override
    public List<Review> findAllReview() {
        return reviewRepositoryImpl.findAllReview();
    }
}
