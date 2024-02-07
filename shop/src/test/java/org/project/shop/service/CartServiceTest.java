package org.project.shop.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.project.shop.domain.Cart;
import org.project.shop.domain.CartItem;
import org.project.shop.domain.Item;
import org.project.shop.domain.Member;
import org.project.shop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
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
    private CartItemServiceImpl cartItemServiceImpl;

    @Autowired
    private CartRepositoryImpl cartRepositoryImpl;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void setUp() {
        Item item1 = new Item("테스트용 책1", 20000, 50);
        Item item2 = new Item("테스트용 책2", 30000, 40);
        itemRepositoryImpl.save(item1);
        itemRepositoryImpl.save(item2);

        Member member = new Member("id1", "password","name1","010-2903-1292","test@test.com");
        memberRepositoryImpl.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartServiceImpl.save(cart);

        CartItem cartItem1 = new CartItem();
        CartItem cartItem2 = new CartItem();

        cartItem1.setItem(item1);
        cartItem1.setCart(cart);
        cartItem1.setCount(3);

        cartItem2.setItem(item2);
        cartItem1.setCart(cart);
        cartItem2.setCount(3);

        cartItemServiceImpl.save(cartItem1);
        cartItemServiceImpl.save(cartItem2);

    }


    @Test
    @DisplayName("장바구니 실제 테스트")
    public void cartSaveTest() {
        Member member = memberRepositoryImpl.findByUserId("id1");

        Cart cart = cartRepositoryImpl.findByMemberId(member.getId());
        List<CartItem> byCartId = cartItemServiceImpl.findByCartId(cart.getId());
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

    }
}