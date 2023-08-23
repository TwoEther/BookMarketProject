package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter

public class OrderItem {
    @Id @GeneratedValue
    private Long id;

    // N:1 (OrderItem : item)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // N:1 (OrderItem : order)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

}
