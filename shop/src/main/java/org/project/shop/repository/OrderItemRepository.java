package org.project.shop.repository;

import org.project.shop.domain.OrderItem;

import java.util.List;

public interface OrderItemRepository {
    public void save(OrderItem orderItem);
    public OrderItem findOrderItemById(Long id);
    public List<OrderItem> findAllOrderItem();
}
