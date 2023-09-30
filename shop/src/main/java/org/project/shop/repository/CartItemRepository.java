package org.project.shop.repository;

import org.project.shop.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository{
    public CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    public CartItem findById(Long id);

    public void save(CartItem cartItem);
}
