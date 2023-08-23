package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Delivery {
    @Id @GeneratedValue
    private Long id;

    private Address address;

    @Enumerated(EnumType.ORDINAL)
    private DeliveryStatus status;

}
