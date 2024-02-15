package org.project.shop.web;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "아이디를 입력하세요")
    private String userId;

    @NotEmpty(message = "비밀번호를 입력하세요")
    private String password1;

//    private String roles;


    @NotEmpty(message = "이름을 입력하세요")
    private String name;

    @NotEmpty(message = "핸드폰 번호를 입력하세요")
    private String phoneNum;

    @NotEmpty(message = "이메일을 입력하세요")
    private String email;

    private String email_Check_number;
//
//    private String city;
//    private String street;
//    private String zipcode;

}
