package org.project.shop.domain;

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

    @Column(name = "user_id", unique = true)
    @NotBlank(message = "아이디는 필수 값 입니다.")
    @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
    private String userId;

    @Column(name = "password")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    @NotBlank(message = "비밀번호는 필수 값 입니다.")
    private String password;

    private String name;

//     정의 타입 사용
    @Embedded
    private Address address;

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

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", grade=" + grade +
                ", orders=" + orders +
                ", reviews=" + reviews +
                '}';
    }

    public Member(String name) {
        this.name = name;
    }

    public Member() {

    }
}
