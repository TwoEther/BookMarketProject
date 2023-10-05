package org.project.shop.service;

import org.project.shop.domain.Cart;
import org.project.shop.domain.CartItem;

public interface CartService {

    public Cart findById(Long id);

    public Long addCart(CartItem cartItem, String memberId);

    public void deleteById(Long id);
}
