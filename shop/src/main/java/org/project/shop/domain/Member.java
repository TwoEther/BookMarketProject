package org.project.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_NUM")
    private Long id;
    private String name;

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member(String name) {
        this.name = name;
    }

}
