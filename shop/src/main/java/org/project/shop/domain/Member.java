package org.project.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter

public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "user_id", unique = true)
    @NotBlank(message = "아이디는 필수 값 입니다.")
    private String userId;

    @Column(name = "password")
    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 값 입니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 값 입니다.")
    private String nickname;

    @NotBlank(message = "핸드폰 번호는 필수 값 입니다.")
    private String phoneNum;

    @NotBlank(message = "이메일은 필수 값 입니다.")
    private String email;

//     정의 타입 사용
    @Embedded
    private Address address;

    private String role;

    @Embedded
    private Grade grade;

    // 1:N (Member : Order)
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
    // 1:N (Member : Review)

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Cart cart;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private LikeItem likeItem;

    public void setLikeItems(LikeItem likeItem) {
        this.likeItem = likeItem;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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

    @Builder
    public Member(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

    public Member(String userId, String password, String nickname, String name, String phoneNum, String email) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    public Member(String name) {
        this.name = name;
    }

    public void setRole(String description) {
        this.role = description;
    }

    public Member() {

    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
