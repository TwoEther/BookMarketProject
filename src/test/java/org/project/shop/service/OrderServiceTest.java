package org.project.shop.service;

import com.querydsl.core.Tuple;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.shop.domain.*;
import org.project.shop.repository.CartItemRepositoryImpl;
import org.project.shop.repository.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.couchbase.AutoConfigureDataCouchbase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.project.shop.domain.QCartItem.cartItem;
import static org.project.shop.domain.QOrderItem.orderItem;

@ActiveProfiles("test")
@AutoConfigureDataCouchbase
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    MemberServiceImpl memberServiceImpl;
    @Autowired
    ItemServiceImpl itemServiceImpl;
    @Autowired
    OrderServiceImpl orderServiceImpl;
    @Autowired
    OrderRepositoryImpl orderRepositoryImpl;

    @Autowired
    OrderItemServiceImpl orderItemServiceImpl;

    @Autowired
    CartServiceImpl cartServiceImpl;
    
    @Autowired
    CartItemServiceImpl cartItemServiceImpl;
    @Autowired
    CartItemRepositoryImpl cartItemRepository;

    @BeforeEach
    public void setUp() {
        Member member1 = new Member("id1", "pw1", "user1", "010-2394-5911", "user1@test.com");
        Member member2 = new Member("id2", "pw2", "user2","010-3952-1860", "user2@test.com");
        memberServiceImpl.save(member1);
        memberServiceImpl.save(member2);

        Item item1 = new Item("book1", 20000, 30);
        Item item2 = new Item("book2", 30000, 40);
        Item item3 = new Item("book3", 26000, 10);
        itemServiceImpl.saveItemNoImage(item1);
        itemServiceImpl.saveItemNoImage(item2);
        itemServiceImpl.saveItemNoImage(item3);

        Cart cart1 = new Cart();
        Cart cart2 = new Cart();
        cart1.setMember(member1);
        cart2.setMember(member2);
        cartServiceImpl.save(cart1);
        cartServiceImpl.save(cart2);

        CartItem cartItemA1 = CartItem.createCartItem(cart1, item1, 3);
        CartItem cartItemA2 = CartItem.createCartItem(cart1, item2, 5);
        cartItemServiceImpl.save(cartItemA1);
        cartItemServiceImpl.save(cartItemA2);
        
        CartItem cartItemB1 = CartItem.createCartItem(cart2, item2, 4);
        CartItem cartItemB2 = CartItem.createCartItem(cart2, item3, 1);
        cartItemServiceImpl.save(cartItemB1);
        cartItemServiceImpl.save(cartItemB2);
        
        
    }

    @DisplayName("구매 테스트")
    @Test
    public void cartItemToOrderItemTest() {
        /*
            ★ 시나리오
                ※ 두명의 맴버가 각각 상품을 장바 구니에 담고 주문을 하려고 한다
                   이때 A는 item1과 2를 주문, B는 item2와 item3를 장바 구니에 담은 상황
                   나머지 주문에 대한 로직 작성
         */
        Member memberA = memberServiceImpl.findByUserId("id1");
        Member memberB = memberServiceImpl.findByUserId("id2");

        Order orderA = new Order();
        Order orderB = new Order();
        orderA.setMember(memberA);
        orderB.setMember(memberB);
        orderServiceImpl.save(orderA);
        orderServiceImpl.save(orderB);

        Cart cartA = cartServiceImpl.findByMemberId(memberA.getId());
        Cart cartB = cartServiceImpl.findByMemberId(memberB.getId());

        List<CartItem> cartItemA = cartItemServiceImpl.findByCartId(cartA.getId());
        List<CartItem> cartItemB = cartItemServiceImpl.findByCartId(cartB.getId());

        for (CartItem cartItem : cartItemA) {
            Item item = cartItem.getItem();
            int count = cartItem.getCount();
            int orderPrice = item.getPrice();
            OrderItem orderItem = OrderItem.createOrderItem(item, orderPrice, count);
            orderItem.setOrder(orderA);
            orderItemServiceImpl.save(orderItem);
        }

        for (CartItem cartItem : cartItemB) {
            Item item = cartItem.getItem();
            int count = cartItem.getCount();
            int orderPrice = item.getPrice();
            OrderItem orderItem = OrderItem.createOrderItem(item, orderPrice, count);
            orderItem.setOrder(orderB);
            orderItemServiceImpl.save(orderItem);
        }

        List<OrderItem> findOrderItemByMemberA = orderItemServiceImpl.findOrderItemByOrderId(orderA.getId());
        List<OrderItem> findOrderItemByMemberB = orderItemServiceImpl.findOrderItemByOrderId(orderB.getId());

        assertThat(findOrderItemByMemberA.size()).isEqualTo(2);
        assertThat(findOrderItemByMemberB.size()).isEqualTo(2);

    }
}
