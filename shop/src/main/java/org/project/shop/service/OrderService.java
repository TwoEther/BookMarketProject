package org.project.shop.service;

import org.project.shop.domain.Order;
import org.project.shop.domain.OrderSearch;

import java.util.List;

public interface OrderService {
    public Long order(Long memberId, Long itemId, int count);
    public void cancelOrder(Long orderId);
    public List<Order> findOrders(OrderSearch orderSearch);

    public void save(Order order);
}
