package org.project.shop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.project.shop.domain.*;
import org.project.shop.domain.Book;
import org.project.shop.repository.OrderRepository;
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
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

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
        Item item = createBook("book1", 10000, 5);
        int orderCount = 2;
        int expectedPrice = 20000;
        //When
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //Then
        Order currentOrder = orderRepository.findOneOrder(orderId);

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
        Item item = createBook("book1", 10000, 5);
        int orderCount = 10;

        //when
        orderService.order(member.getId(), item.getId(), orderCount);
        fail("재고 초과");

        //then
    }

    @Test
    public void cancelOrder(){
        //given
        Member member = createMember();
        Item item = createBook("book1", 10000, 5);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        Order currentOrder = orderRepository.findOneOrder(orderId);

        //when
        currentOrder.cancel();

        //then
        assertThat(OrderStatus.CANCEL).isEqualTo(currentOrder.getStatus());
        assertThat(item.getStockQuantity()).isEqualTo(5);

    }


    private Item createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("경기", "고양", "19234"));
        em.persist(member);
        return member;
    }
}
