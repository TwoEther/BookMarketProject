package org.project.shop.repository;

import org.project.shop.domain.Order;
import org.project.shop.domain.OrderSearch;

import java.util.List;

public interface OrderRepository {
    public void save(Order order);

    public Order findOneOrder(Long id);

    public List<Order> findAllOrder(OrderSearch orderSearch);
}
