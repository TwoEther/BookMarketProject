package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter

public class OrderItem {
    @Id @GeneratedValue
    @Column(name="orderItem_id")
    private Long id;

    // N:1 (OrderItem : item)
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // N:1 (OrderItem : order)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "orderItems")
    private Order order;

    private int orderPrice;
    private int count;

    public void setItem(Item item) {
        this.item = item;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setOrder(Order order) {
        if (this.order != null) {
            this.order.getOrderItems().remove(this);
        }
        this.order = order;
        order.getOrderItems().add(this);

    }

    // 생성 메소드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

//        item.removeStock(count);
        return orderItem;
    }

    // 주문 취소
    public void cancel() {
        getItem().addStock(count);
    }

    // 주문 아이템 조회
    public static List<Item> findItems(List<OrderItem> orderItems) {
        List<Item> findItems = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Item item = orderItem.getItem();
            findItems.add(item);
        }
        return findItems;
    }
    // 주문상품 가격 조회
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }




    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderPrice=" + orderPrice +
                ", count=" + count +
                '}';
    }
}
