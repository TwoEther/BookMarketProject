package org.project.shop.service;

import lombok.RequiredArgsConstructor;
import org.project.shop.domain.*;
import org.project.shop.repository.ItemRepository;
import org.project.shop.repository.MemberRepository;
import org.project.shop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    // 주문
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findMember(memberId);
        Item item = itemRepository.findOneItem(itemId);

        // 배송지 설정
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    // 주문 취소
    public void cancelOrder(Long orderId) {
        // 주문 조회
        Order order = orderRepository.findOneOrder(orderId);
        order.cancel();
    }


    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllOrder(orderSearch);
    }
}
