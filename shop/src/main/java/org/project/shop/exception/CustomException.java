package org.project.shop.exception;

import lombok.Getter;

@Getter

public class CustomException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public CustomException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
