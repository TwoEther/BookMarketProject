package org.project.shop.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
    private MemberServiceImpl memberServiceImpl;
    @Autowired
    private MemberRepositoryImpl memberRepositoryImpl;

    @Autowired
    private ItemRepositoryImpl itemRepositoryImpl;

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private CartServiceImpl cartServiceImpl;
    @Autowired
    private CartItemRepositoryImpl cartItemRepositoryImpl;

    @Autowired
    private CartRepositoryImpl cartRepositoryImpl;

    @PersistenceContext
    EntityManager em;


    public Member createMember() {
        Member member = new Member("id", "password");
        memberRepositoryImpl.save(member);
        return member;
    }

    public Item createItem() {
        Item item = new Item();
        item.setName("테스트책");
        item.setPrice(20000);
        item.setStockQuantity(20);

        itemRepositoryImpl.save(item);
        return item;
    }

    @Test
    @DisplayName("단순 장바구니 테스트")
    public void saveTest() {
        Member member = createMember();
        Item item = createItem();

        CartItem cartItem = new CartItem();
        cartItem.setCount(5);
        cartItem.setItem(item);


        Long cartItemId = cartServiceImpl.addCart(cartItem, member.getUserId());
        CartItem findCartItem = cartItemRepositoryImpl.findById(cartItemId);

        assertThat(item.getId()).isEqualTo(cartItem.getItem().getId());
        assertThat(cartItem.getCount()).isEqualTo(findCartItem.getCount());

    }

    public void setUp() {
        Item item1 = new Item("테스트용 책1", 20000, 50);
        Item item2 = new Item("테스트용 책2", 30000, 40);
        itemRepositoryImpl.save(item1);
        itemRepositoryImpl.save(item2);

        Member member = new Member("lee", "pw1");
        memberRepositoryImpl.save(member);

    }

    @Test
    @DisplayName("장바구니 실제 테스트")
    public void cartSaveTest() {
        setUp();

        Member member = memberRepositoryImpl.findByUserId("lee");

        // 1. 웹에서 상품에 대한 정보를 받음
        String inputItemName = "테스트용 책1";
        int inputValue = 10;
        String currentUserName = "lee";

        Cart cart = cartRepositoryImpl.findByMemberId(member.getId());
        CartItem saveCartItem = cartItemRepositoryImpl.findByCartIdAndItemId(cart.getId(), member.getId());
    }

    @Test
    @DisplayName("중복된 아이템이 들어왔을때 처리")
    public void duplicatedAddItemTest() {
        Member member1 = new Member("id1", "pw1");
        member1.setName("id1");
        memberRepositoryImpl.save(member1);

        Member member2 = new Member("id2", "pw2");
        memberRepositoryImpl.save(member2);

        Cart cart1 = new Cart();
        cart1.setMember(member1);
        cartRepositoryImpl.save(cart1);

        Cart cart2 = new Cart();
        cart2.setMember(member2);
        cartRepositoryImpl.save(cart2);

        String name = "name" + Integer.toString(1);
        int price = (int) (Math.random() * 30000) + 10000;
        int stockQuantity = (int) (Math.random() * 100);

        Item item1 = new Item(name, price, stockQuantity);
        Item item2 = new Item(name, price, stockQuantity);
        itemRepositoryImpl.save(item1);
        itemRepositoryImpl.save(item2);

        CartItem cartItem1 = CartItem.createCartItem(cart1, item1, 5);
        CartItem cartItem2 = CartItem.createCartItem(cart2, item1, 3);

        /*
            중복된 아이템을 가진 데이터가 들어왔을때 기존 데이터에 Add 해준다
         */
        cartItemRepositoryImpl.save(cartItem1);


    }
}