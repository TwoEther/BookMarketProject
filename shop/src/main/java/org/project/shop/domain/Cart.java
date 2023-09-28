package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;
    private int item_price;

    public Cart() {
    }

    public Cart(int item_price) {
        this.item_price = item_price;
    }

    public Cart(Long cartId, int item_price) {
        this.id = cartId;
        this.item_price = item_price;
    }


    @OneToOne(mappedBy = "cart", cascade = CascadeType.MERGE)
    private Member member;

    @OneToMany(mappedBy = "cart")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "cart")
    private List<Order> orders = new ArrayList<>();

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + id +
                ", item_price=" + item_price +
                ", member = " + member.getId() +
                '}';
    }


    public void setMember(Member member) {
        this.member = member;
        member.setCart(this);
    }

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
