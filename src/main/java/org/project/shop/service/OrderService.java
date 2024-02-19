package org.project.shop.service;

import org.project.shop.domain.Order;
import org.project.shop.domain.OrderSearch;

import java.util.List;

public interface OrderService {
    public Long order(Long memberId, Long itemId, int count);
    public void cancelOrder(Long orderId);

    public List<Order> findAllOrder();
    public List<Order> findOrders(OrderSearch orderSearch);
    public Order findByOrderId(Long orderId);

    public Order findByMemberIdBeforePayment(Long memberId);
    public List<Order> findByMemberIdAfterPayment(Long memberId);

    public Order findByMemberIdAfterPaymentOneOrder(Long orderId, Long memberId);

    public List<Order> findOrderByMemberId(Long memberId);

    public void deleteOrder(Long orderId);

    public void save(Order order);
}
