package com.example.crud.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private int status;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .code(String.valueOf(errorCode.getHttpStatus()))
                        .message(errorCode.getDescription())
                        .status(errorCode.getHttpStatus().value())
                        .build());
    }
}
