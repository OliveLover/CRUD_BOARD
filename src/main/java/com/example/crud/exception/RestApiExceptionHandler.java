package com.example.crud.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
글로벌 예외처리 핸들러
각각의 에러에 따라
HttpStatus, ErrorCode, 메시지를 반환한다.
 */

@RestControllerAdvice
public class RestApiExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler (CustomException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

}
