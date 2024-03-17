package org.project.shop.repository;

import org.project.shop.domain.Order;
import org.project.shop.domain.OrderItem;
import org.project.shop.domain.OrderSearch;

import java.util.List;

public interface OrderRepository {
    public void save(Order order);

    public Order findByOrderId(Long orderId);
    public List<Order> findAllOrder();

    public Order findByMemberIdBeforePayment(Long memberId);

    public List<Order> findByMemberIdAfterPayment(Long memberId);

    public Order findByMemberIdAfterPaymentOneOrder(Long orderId, Long memberId);

    public List<Order> findOrderByMemberId(Long memberId);

    public void deleteAllOrder();

    public void deleteByMemberId(Long memberId);

    public void deleteOrder(Long orderId);


    public List<Order> findAllOrder(OrderSearch orderSearch);
}
