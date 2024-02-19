package org.project.shop.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor

public enum MemberExceptionCode {
    EMPTY("공백값"),
    Reqex("정규표현식위반"),
    Dup("중복"),
    OK("사용가능");

    private String description;

}
