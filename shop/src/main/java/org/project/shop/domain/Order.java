package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
// MySQL에서 order는 예약어 이므로 orders로 이름 변경
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    // 지연 로딩 사용
    // N:1 (Order : Member)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orders")
    private Member member;

    // 1:N (Order : OrderItem)
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 1:N (Order : Delivery)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDate orderDate;

    // Enum type
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;
}
