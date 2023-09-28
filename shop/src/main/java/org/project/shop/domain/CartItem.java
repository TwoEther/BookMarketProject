package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CartItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Cart cart;

    private int count;

    public void setItem(Item item) {
        this.item = item;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }


    public void addCount(int count) {
        this.count += count;
    }
}
