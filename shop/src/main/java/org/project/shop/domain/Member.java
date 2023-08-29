package org.project.shop.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_NUM")
    private Long id;

    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    private String name;

//     정의 타입 사용
    @Embedded
    private Address address;

    // 1:N (Member : Order)
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    // 1:N (Member : Review)
    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member(String name) {
        this.name = name;
    }

    public Member() {

    }
}
