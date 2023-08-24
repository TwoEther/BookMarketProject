package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Delivery {
    @Id @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    private Address address;

    @Enumerated(EnumType.ORDINAL)
    private DeliveryStatus status;


    public void setOrder(Order order) {
        this.order = order;
    }

}
