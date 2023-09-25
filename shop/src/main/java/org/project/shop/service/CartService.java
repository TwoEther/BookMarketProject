package org.project.shop.service;

import org.project.shop.domain.Cart;

public interface CartService {

    public void save(Cart cart);
    public Cart findById(Long id);
}
