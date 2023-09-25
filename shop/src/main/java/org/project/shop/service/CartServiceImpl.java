package org.project.shop.service;

import org.project.shop.domain.Cart;
import org.project.shop.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id);
    }
}
