package org.project.shop.service;

import com.querydsl.core.Tuple;
import org.project.shop.domain.CartItem;
import org.project.shop.domain.Item;

import java.util.List;

public interface CartItemService {
    public CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    public CartItem findByCartItemId(Long id);

    public void save(CartItem cartItem);

    public void clear();

    public List<CartItem> findAllCartItem();


    public List<CartItem> findByCartId(Long memberId);

    public List<CartItem> findCartItemByItem(Long itemId);

    public List<Tuple> findItemIdByCartId(Long cartId);

    public void deleteById(Long id);

}
