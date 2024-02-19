package org.project.shop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public enum Role {
    ROLE_USER("일반 유저"),
    ROLE_ANONYMOUS("비로그인 유저"),
    ROLE_ADMIN("관리자");

    private final String description;

    Role(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }
}
