package org.project.shop.repository;

import org.project.shop.domain.LikeItem;

import java.util.List;

public interface LikeItemRepository {
    public void save(LikeItem likeItem);
    public LikeItem findLikeItemByMemberId(String userId);

    public List<LikeItem> findAllLikeItem();
}
