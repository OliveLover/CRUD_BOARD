package com.example.crud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class RestApiExceptionHandler {
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleApiRequestException(IllegalArgumentException ex) {
        RestApiException restApiException = new RestApiException();
        restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        restApiException.setErrorCode("400 error");
        restApiException.setErrorMessage("잘못된 요청입니다.");

        return new ResponseEntity<>(restApiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        RestApiException restApiException = new RestApiException();
        restApiException.setHttpStatus(HttpStatus.NOT_FOUND);
        restApiException.setErrorCode("404 error");
        restApiException.setErrorMessage("사용자를 찾을 수 없습니다.");

        return new ResponseEntity<>(restApiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
        RestApiException restApiException = new RestApiException();
        restApiException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        restApiException.setErrorCode("500 error");
        restApiException.setErrorMessage("서버에러(로그인 상태를 확인하세요.)");

        return new ResponseEntity<>(restApiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(value = {HttpClientErrorException.Forbidden.class})
//    public ResponseEntity<Object> handleForbiddenException(HttpClientErrorException.Forbidden ex) {
//        RestApiException restApiException = new RestApiException();
//        restApiException.setHttpStatus(HttpStatus.FORBIDDEN);
//        restApiException.setErrorCode("403 error");
//        restApiException.setErrorMessage("서버에서 허락되지 않은 요청입니다.");
//
//        return new ResponseEntity<>(restApiException, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        RestApiException restApiException = new RestApiException();
        restApiException.setHttpStatus(HttpStatus.FORBIDDEN);
        restApiException.setErrorCode("403 error");
        restApiException.setErrorMessage("해당 요청에 대한 권한이 없습니다.");

        return new ResponseEntity<>(restApiException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {HttpClientErrorException.NotAcceptable.class})
    public ResponseEntity<Object> handleNotAcceptableException(HttpClientErrorException.NotAcceptable ex) {
        RestApiException restApiException = new RestApiException();
        restApiException.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
        restApiException.setErrorCode("406 error");
        restApiException.setErrorMessage("적절하지 않은 요청입니다.");

        return new ResponseEntity<>(restApiException, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        RestApiException restApiException = new RestApiException();
        restApiException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        restApiException.setErrorCode("500 error");
        restApiException.setErrorMessage("서버 에러 발생");

        return new ResponseEntity<>(restApiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
