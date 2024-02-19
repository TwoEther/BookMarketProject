package org.project.shop.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

// Exception 처리를 위한 클래스
@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ExceptionCode exceptionCode) {
        return ResponseEntity
                .status(exceptionCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(exceptionCode.getHttpStatus().value())
                        .error(exceptionCode.getHttpStatus().name())
                        .code(exceptionCode.name())
                        .message(exceptionCode.getMessage())
                        .build()
                );
    }
}
