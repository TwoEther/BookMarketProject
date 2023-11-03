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
    @Column(name = "order_id")
    private Long id;

    @Column(unique = true)
    private String tid;

    // 지연 로딩 사용
    // N:1 (Order : Member)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orders")
    private Member member;

    // 1:N (Order : OrderItem)
    @OneToMany(mappedBy = "order", cascade = CascadeType.MERGE)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 1:N (Order : Delivery)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    // N:1 (Order : Cart)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private Cart cart;

    private LocalDate orderDate;

    // Enum type
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    // 연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }


    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    public void setTid(String tid) {
        this.tid = tid;
    }

    public static Order createOrder(Member member){
        Order order = new Order();
        order.setMember(member);
        order.setStatus(OrderStatus.READY);
        order.setTid("");
        order.setOrderDate(LocalDate.now());
        return order;
    }

    // 주문취소
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송된 상품은 취소가 불가능합니다");
        }
        this.setStatus(OrderStatus.READY);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }



    // 주문 가격 조회
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member +
                ", orderItems=" + orderItems +
                '}';
    }
}
