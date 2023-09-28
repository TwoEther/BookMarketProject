package org.project.shop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.shop.domain.Cart;
import org.project.shop.domain.CartItem;
import org.project.shop.domain.Item;
import org.project.shop.domain.Member;
import org.project.shop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CartServiceTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        Member member = new Member("id", "password");
        memberRepository.save(member);
        return member;
    }

    public Item createItem() {
        Item item = new Item();
        item.setName("테스트책");
        item.setPrice(20000);
        item.setStockQuantity(20);

        itemRepository.save(item);
        return item;
    }

    @Test
    @DisplayName("장바구니 테스트")
    public void saveTest() {
        Member member = createMember();
        Item item = createItem();

        CartItem cartItem = new CartItem();
        cartItem.setCount(5);
        cartItem.setItem(item);


        Long cartItemId = cartService.addCart(cartItem, member.getUserId());
        Optional<CartItem> findCartItem = cartItemRepository.findById(cartItemId);

        assertThat(item.getId()).isEqualTo(cartItem.getItem().getId());
        assertThat(cartItem.getCount()).isEqualTo(findCartItem.get().getCount());

    }



}