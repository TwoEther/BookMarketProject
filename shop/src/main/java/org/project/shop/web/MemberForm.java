package org.project.shop.web;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "이름을 입력하세요")
    private String name;

    private String city;
    private String street;
    private String zipcode;

}
