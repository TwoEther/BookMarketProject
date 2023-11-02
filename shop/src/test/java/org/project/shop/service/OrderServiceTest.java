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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.project.shop.domain.QCartItem.cartItem;

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

    @DisplayName("장바구니를 거치지 않는 단일 품목 구매 테스트")
    @Test
    public void singleItemPurchaseTest() {
        /*
            1. 회원이 하나의 아이템을 여러개 구매
            2. 구매한 아이템이 OrderRepository에 저장되어 있어야함.
         */
        Member member1 = new Member("id1", "pw1");
        Member member2 = new Member("id2", "pw2");
        memberServiceImpl.join(member1);
        memberServiceImpl.join(member2);

        Item item1 = new Item("item1", 20000, 30);
        Item item2 = new Item("item2", 34000, 10);
        itemServiceImpl.saveItemNoImage(item1);
        itemServiceImpl.saveItemNoImage(item2);

        String memberId1 = "id1";

        // 특정 회원이 물품 구입
        /*
            OrderItem 에서 물품 구매를 등록, Order 에서 이를 모아 처리
         */
        Member findMember1 = memberServiceImpl.findByUserId(memberId1);
        Order order1 = new Order();
        Cart cart1 = new Cart();
        cartServiceImpl.save(cart1);

        order1.setMember(findMember1);
        order1.setCart(cart1);
        orderServiceImpl.save(order1);
        OrderItem orderItem = OrderItem.createOrderItem(item1, 20000, 3);
        orderItem.setOrder(order1);

        orderItemServiceImpl.save(orderItem);
        List<OrderItem> findAllOrderItem = orderItemServiceImpl.findAllOrderItem();
        assertThat(findAllOrderItem.size()).isEqualTo(1);
        for (OrderItem item : findAllOrderItem) {
            System.out.println("item.toString() = " + item.toString());
        }
    }

    @DisplayName("장바구니 상품이 주문 엔티티로 전환 테스트")
    @Test
    public void cartItemToOrderItemTest() {
        // 요구 조건
        /*
            1. 서로 다른 맴버에 대해서 장바 구니 목록이 공유 되면 안됨
            2. 장바 구니 상태의 아이템 이 구매 목록에 전부 포함 되어야 함
         */

        Member member1 = new Member("id1", "pw1");
        Member member2 = new Member("id2", "pw2");

        memberServiceImpl.join(member1);
        memberServiceImpl.join(member2);

        int stock1 = 30;
        int stock2 = 40;

        Item book1 = new Item("book1", 20000, stock1);
        Item book2 = new Item("book2", 32000, stock2);
        itemServiceImpl.saveItemNoImage(book1);
        itemServiceImpl.saveItemNoImage(book2);

        Cart cart1 = new Cart();
        Cart cart2 = new Cart();
        cart1.setMember(member1);
        cart2.setMember(member2);

        cartServiceImpl.save(cart1);
        cartServiceImpl.save(cart2);

        int count1 = 3;
        int count2 = 5;

        CartItem cartItem1 = CartItem.createCartItem(cart1, book1, count1);
        CartItem cartItem2 = CartItem.createCartItem(cart2, book2, count2);

        cartItemRepository.save(cartItem1);
        cartItemRepository.save(cartItem2);

        book1.setStockQuantity(stock1-count1);
        book2.setStockQuantity(stock2-count2);

        List<Tuple> findItemIds = cartItemServiceImpl.findItemIdByCartId(cart1.getId());
        Order order = Order.createOrder(member1);
        orderServiceImpl.save(order);
        
        for (Tuple tuple : findItemIds) {
            Item item = tuple.get(cartItem.item);
            int count = tuple.<Integer>get(cartItem.count);

            OrderItem orderItem = OrderItem.createOrderItem(item, item != null ? item.getPrice() : 0, count);
            orderItem.setOrder(order);
            orderItemServiceImpl.save(orderItem);
        }

        List<OrderItem> allOrderItem = orderItemServiceImpl.findAllOrderItem();
//        assertThat(allOrderItem.size()).isEqualTo(findItemIds.size());

        List<OrderItem> orderItemByOrderAndItem = orderItemServiceImpl.findOrderItemByOrderAndItem(order.getId(), book1.getId());
        for (OrderItem orderItem : allOrderItem) {
            System.out.println("orderItem.toString() = " + orderItem.toString());
        }
        
        for (OrderItem orderItem : orderItemByOrderAndItem) {
            System.out.println("orderItemByOrderAndItem.toString() = " + orderItem.toString());
        }

    }
}
