package org.project.shop.service;

import org.project.shop.domain.OrderItem;

import java.util.List;

public interface OrderItemService {
    public void save(OrderItem orderItem);

    public OrderItem findOrderItemById(Long id);

    public List<OrderItem> findOrderItemByOrderId(Long orderId);
    public List<OrderItem> findOrderItemByItemId(Long itemId);

    public List<OrderItem> findOrderItemByOrderAndItem(Long orderId, Long itemId);

    public List<OrderItem> findByOrderAndItemAfterPayment(Long orderId, Long itemId);
    public List<OrderItem> findAllOrderItem();

    public void deleteOrderItem(Long orderId);
}
