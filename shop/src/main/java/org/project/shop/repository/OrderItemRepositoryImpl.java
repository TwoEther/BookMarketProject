package org.project.shop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.project.shop.domain.OrderItem;
import org.project.shop.domain.OrderStatus;
import org.project.shop.domain.QOrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.project.shop.domain.QOrderItem.orderItem;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository{
    @PersistenceContext
    EntityManager em;

    private final JPAQueryFactory queryFactory;

    public OrderItemRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void save(OrderItem orderItem) {
        em.persist(orderItem);
    }

    @Override
    public OrderItem findOrderItemById(Long id) {
        return queryFactory.selectFrom(orderItem)
                .where(orderItem.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<OrderItem> findOrderItemByOrderId(Long orderId) {
        return queryFactory.selectFrom(orderItem)
                .where(orderItem.order.id.eq(orderId))
                .orderBy(orderItem.order.orderDate.desc())
                .fetch();
    }

    @Override
    public List<OrderItem> findOrderItemByItemId(Long itemId) {
        return queryFactory.selectFrom(orderItem)
                .where(orderItem.item.id.eq(itemId))
                .fetch();
    }

    @Override
    public List<OrderItem> findOrderItemByOrderAndItem(Long orderId, Long itemId) {
        return queryFactory.selectFrom(orderItem)
                .where(orderItem.order.id.eq(orderId).and(
                        orderItem.item.id.eq(itemId).and(
                                orderItem.order.status.eq(OrderStatus.READY)
                        )
                )).fetch();
    }

    @Override
    public List<OrderItem> findByOrderAndItemAfterPayment(Long orderId, Long itemId) {
        return queryFactory.selectFrom(orderItem)
                .where(orderItem.order.id.eq(orderId).and(
                        orderItem.item.id.eq(itemId).and(
                                orderItem.order.status.eq(OrderStatus.SUCCESS)
                        )
                )).fetch();
    }


    @Override
    public List<OrderItem> findAllOrderItem() {
        return queryFactory.selectFrom(orderItem)
                .fetch();
    }
}
