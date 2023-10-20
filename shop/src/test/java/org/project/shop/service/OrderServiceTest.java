package org.project.shop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.shop.domain.*;
import org.project.shop.repository.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.couchbase.AutoConfigureDataCouchbase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

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
}
