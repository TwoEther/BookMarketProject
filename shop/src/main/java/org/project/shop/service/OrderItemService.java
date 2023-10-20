package org.project.shop.service;

import org.project.shop.domain.OrderItem;

import java.util.List;

public interface OrderItemService {
    public void save(OrderItem orderItem);

    public OrderItem findOrderItemById(Long id);
    public List<OrderItem> findAllOrderItem();
}
