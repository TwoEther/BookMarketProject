package org.project.shop.service;

import com.querydsl.core.Tuple;
import org.project.shop.domain.CartItem;
import org.project.shop.domain.Item;
import org.project.shop.repository.CartItemRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService{
    private final CartItemRepositoryImpl cartItemRepositoryImpl;

    public CartItemServiceImpl(CartItemRepositoryImpl cartItemRepositoryImpl) {
        this.cartItemRepositoryImpl = cartItemRepositoryImpl;
    }

    @Override
    public CartItem findByCartIdAndItemId(Long cartId, Long itemId) {
        return cartItemRepositoryImpl.findByCartIdAndItemId(cartId, itemId);
    }

    @Override
    public CartItem findByCartItemId(Long id) {
        return cartItemRepositoryImpl.findByCartItemId(id);
    }

    @Override
    public void save(CartItem cartItem) {
        cartItemRepositoryImpl.save(cartItem);
    }

    @Override
    public void clear() {
        cartItemRepositoryImpl.clear();
    }

    @Override
    public List<CartItem> findAllCartItem() {
        return cartItemRepositoryImpl.findAllCartItem();
    }

    @Override
    public List<CartItem> findByCartId(Long cartId) {
        return cartItemRepositoryImpl.findByCartId(cartId);
    }

    @Override
    public List<CartItem> findCartItemByItem(Long itemId) {
        return cartItemRepositoryImpl.findCartItemByItem(itemId);
    }

    @Override
    public List<Tuple> findItemIdByCartId(Long cartId) {
        return cartItemRepositoryImpl.findItemIdByCartId(cartId);
    }

    @Override
    public void deleteAll() {
        cartItemRepositoryImpl.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepositoryImpl.deleteById(id);
    }
}
