package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter

public class OrderItem {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

}
