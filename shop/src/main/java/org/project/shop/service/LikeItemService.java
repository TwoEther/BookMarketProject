package org.project.shop.service;

import org.project.shop.domain.LikeItem;

import java.util.List;

public interface LikeItemService {
    public void save(LikeItem likeItem);
    public LikeItem findLikeItemByMemberId(String userId);

    public List<LikeItem> findAllLikeItem();
}
