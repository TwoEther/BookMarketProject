package org.project.shop.service;

import org.project.shop.domain.LikeReview;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LikeReviewService {
    public void save(LikeReview likeReview);

    public LikeReview findByMemberId(Long memberId);

    public LikeReview findByReviewIdAndMemberId(Long reviewId, Long memberId);

    public List<LikeReview> findAllLikeReview();

    public void deleteLikeReview(Long id);

}
