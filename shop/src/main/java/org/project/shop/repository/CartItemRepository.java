package org.project.shop.repository;

import org.project.shop.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository{
    public CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    public CartItem findByCartItemId(Long id);

    public void save(CartItem cartItem);

    public void clear();

    public List<CartItem> findAllCartItem();


    public List<CartItem> findByCartId(Long memberId);

    public List<CartItem> findCartItemByItem(Long itemId);

    public void deleteById(Long id);

}
