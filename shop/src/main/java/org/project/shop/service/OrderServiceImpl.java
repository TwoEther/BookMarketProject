package org.project.shop.service;

import lombok.RequiredArgsConstructor;
import org.project.shop.domain.*;
import org.project.shop.repository.ItemRepositoryImpl;
import org.project.shop.repository.MemberRepositoryImpl;
import org.project.shop.repository.OrderRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final MemberRepositoryImpl memberRepository;
    private final OrderRepositoryImpl orderRepositoryImpl;
    private final ItemRepositoryImpl itemRepositoryImpl;

    // 주문
    @Override
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findMember(memberId);
        Item item = itemRepositoryImpl.findOneItem(itemId);

        // 배송지 설정
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member);

        // 주문 저장
        orderRepositoryImpl.save(order);
        return order.getId();
    }
    @Override
    // 주문 취소
    public void cancelOrder(Long orderId) {
        // 주문 조회
        Order order = orderRepositoryImpl.findByOrderId(orderId);
        order.cancel();
    }

    @Override
    public List<Order> findAllOrder() {
        return orderRepositoryImpl.findAllOrder();
    }

    @Override
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepositoryImpl.findAllOrder(orderSearch);
    }

    @Override
    public Order findByOrderId(Long orderId) {
        return orderRepositoryImpl.findByOrderId(orderId);
    }

    @Override
    public Order findByMemberIdBeforePayment(Long memberId) {
        return orderRepositoryImpl.findByMemberIdBeforePayment(memberId);
    }

    @Override
    public List<Order> findByMemberIdAfterPayment(Long memberId) {
        return orderRepositoryImpl.findByMemberIdAfterPayment(memberId);
    }

    @Override
    public List<Order> findOrderByMemberId(Long memberId) {
        return orderRepositoryImpl.findOrderByMemberId(memberId);
    }

    @Override
    @Transactional
    public void save(Order order) {
        orderRepositoryImpl.save(order);
    }
}
