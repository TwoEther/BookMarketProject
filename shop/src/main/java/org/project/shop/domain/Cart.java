package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Cart {
    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long cartId;
    private int item_price;

    public Cart() {
    }

    public Cart(int item_price) {
        this.item_price = item_price;
    }

    public Cart(Long cartId, int item_price) {
        this.cartId = cartId;
        this.item_price = item_price;
    }

    public Cart(Long cartId, int item_price, Member member) {
        this.cartId = cartId;
        this.item_price = item_price;
        this.member = member;
    }

    @OneToOne(mappedBy = "cart")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        member.setCart(this);
    }

    @OneToMany(mappedBy = "cart")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "cart")
    private List<Order> orders = new ArrayList<>();
}
