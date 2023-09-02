package org.project.shop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.project.shop.domain.*;
import org.project.shop.repository.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.couchbase.AutoConfigureDataCouchbase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@AutoConfigureDataCouchbase
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderServiceImpl orderServiceImpl;
    @Autowired
    OrderRepositoryImpl orderRepositoryImpl;

    /*
        테스트 조건
        1. 상품 주문이 성공되어야 함
        2. 상품을 주문할때 재고 수량을 초과하면 안됨
        3. 주문 취소가 성공
     */

    @Test
    public void orderTest(){
        //Given
        Member member = createMember();
        Item item = createitem("item1", 10000, 5);
        int orderCount = 2;
        int expectedPrice = 20000;
        //When
        Long orderId = orderServiceImpl.order(member.getId(), item.getId(), orderCount);

        //Then
        Order currentOrder = orderRepositoryImpl.findOneOrder(orderId);

        /*
            1. 주문 상태는 ORDER
            2. 상품 주문 종류는 1
            3. 주문 가격은 주문 개수 * 상품 가격
            4. 재고 개수는 주문 수량 만큼 감소 해야함
         */
        assertThat(OrderStatus.ORDER).isEqualTo(currentOrder.getStatus());
        assertThat(currentOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(20000).isEqualTo(currentOrder.getTotalPrice());
        assertThat(item.getStockQuantity()).isEqualTo(5 - orderCount);
    }

    @Test
    public void overStockQuantity() throws Exception {
        //given
        Member member = createMember();
        Item item = createitem("item1", 10000, 5);
        int orderCount = 10;

        //when
        orderServiceImpl.order(member.getId(), item.getId(), orderCount);
        fail("재고 초과");

        //then
    }

    @Test
    public void cancelOrder(){
        //given
        Member member = createMember();
        Item item = createitem("item1", 10000, 5);
        int orderCount = 2;
        Long orderId = orderServiceImpl.order(member.getId(), item.getId(), orderCount);
        Order currentOrder = orderRepositoryImpl.findOneOrder(orderId);

        //when
        currentOrder.cancel();

        //then
        assertThat(OrderStatus.CANCEL).isEqualTo(currentOrder.getStatus());
        assertThat(item.getStockQuantity()).isEqualTo(5);

    }

    @Test
    private Item createitem(String name, int price, int stockQuantity) {
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        em.persist(item);
        return item;
    }
    @Test
    private Member createMember() {
        Member member = new Member();
        member.setName("member1");
//        member.setAddress(new Address("경기", "고양", "19234"));
        em.persist(member);
        return member;
    }
}
