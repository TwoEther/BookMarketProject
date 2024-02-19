package org.project.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SaleStatus {
    Enabled("판매 가능"),
    Disabled("판매 불가능");

    private String description;
}
