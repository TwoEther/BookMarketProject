package org.project.shop.repository;

import com.querydsl.core.Tuple;
import org.project.shop.domain.CartItem;

import java.util.List;

public interface CartItemRepository{
    public CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    public CartItem findByCartItemId(Long id);

    public void save(CartItem cartItem);

    public void clear();

    public List<CartItem> findAllCartItem();


    public List<CartItem> findByCartId(Long memberId);

    public List<CartItem> findCartItemByItem(Long itemId);

    public List<Tuple> findItemIdByCartId(Long cartId);

    public void deleteAll();
    public void deleteById(Long id);

}
