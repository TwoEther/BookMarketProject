package org.project.shop.service;

import org.project.shop.domain.Cart;
import org.project.shop.domain.CartItem;
import org.project.shop.domain.Item;
import org.project.shop.domain.Member;

public interface CartService {

    public Cart findById(Long id);

    public Long addCart(Member findMember, Item findItem, int quantity);

    public Cart findByMemberId(Long memberId);

    public void deleteById(Long id);

    public void deleteAll();

    public void save(Cart cart);
}
