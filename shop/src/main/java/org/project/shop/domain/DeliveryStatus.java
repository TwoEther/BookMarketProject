package org.project.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum DeliveryStatus {
    READY("배송 준비중"),
    GOING("배송중"),
    COMPLETE("배송 완료"),
    NOTFOUND("추적 불가능");

    private String description;
}
