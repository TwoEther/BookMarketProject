package org.project.shop.repository;

import org.project.shop.domain.LikeReview;

import java.util.List;

public interface LikeReviewRepository {
    public void save(LikeReview likeReview);

    public LikeReview findByMemberId(Long memberId);

    public LikeReview findByReviewIdAndMemberId(Long reviewId, Long memberId);

    public List<LikeReview> findAllLikeReview();
    public void deleteLikeReview(Long id);
}
