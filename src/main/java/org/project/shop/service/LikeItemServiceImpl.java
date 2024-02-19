package org.project.shop.service;

import org.project.shop.domain.LikeItem;
import org.project.shop.repository.LikeItemRepository;
import org.project.shop.repository.LikeItemRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeItemServiceImpl implements LikeItemService{
    private final LikeItemRepositoryImpl likeItemRepositoryimpl;

    public LikeItemServiceImpl(LikeItemRepositoryImpl likeItemRepositoryimpl) {
        this.likeItemRepositoryimpl = likeItemRepositoryimpl;
    }

    @Override
    public void save(LikeItem likeItem) {
        likeItemRepositoryimpl.save(likeItem);
    }

    @Override
    public List<LikeItem> findLikeItemByMemberId(Long memberId) {
        return likeItemRepositoryimpl.findLikeItemByMemberId(memberId);
    }

    @Override
    public List<LikeItem> findAllLikeItem() {
        return likeItemRepositoryimpl.findAllLikeItem();
    }
}
