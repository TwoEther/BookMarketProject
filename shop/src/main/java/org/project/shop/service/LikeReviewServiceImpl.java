package org.project.shop.service;

import lombok.RequiredArgsConstructor;
import org.project.shop.domain.LikeReview;
import org.project.shop.repository.LikeReviewRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeReviewServiceImpl implements LikeReviewService{
    private final LikeReviewRepositoryImpl LikeReviewRepositoryImpl;
    @Override
    @Transactional
    public void save(LikeReview likeReview) {
        LikeReviewRepositoryImpl.save(likeReview);
    }

    @Override
    public LikeReview findByMemberId(Long memberId) {
        return LikeReviewRepositoryImpl.findByMemberId(memberId);
    }

    @Override
    public LikeReview findByReviewIdAndMemberId(Long reviewId, Long memberId) {
        return LikeReviewRepositoryImpl.findByReviewIdAndMemberId(reviewId, memberId);
    }

    @Override
    public List<LikeReview> findAllLikeReview() {
        return LikeReviewRepositoryImpl.findAllLikeReview();
    }

    @Override
    public void deleteLikeReview(Long id) {
        LikeReviewRepositoryImpl.deleteLikeReview(id);
    }
}
