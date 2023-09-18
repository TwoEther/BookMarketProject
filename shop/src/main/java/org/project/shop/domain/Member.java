package org.project.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMBER_NUM")
    private Long id;

    @Column(name = "userId", unique = true)
    @NotBlank(message = "아이디는 필수 값 입니다.")
    private String userId;

    @Column(name = "password")
    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    private String password;

    private String name;



//     정의 타입 사용
    @Embedded
    private Address address;

    @Column(name = "Role")
    @Embedded
    private Role role;

    @Embedded
    private Grade grade;

    // 1:N (Member : Order)
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
    // 1:N (Member : Review)
    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Member(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }


    public Member(String name) {
        this.name = name;
    }

    public Member() {

    }
}
