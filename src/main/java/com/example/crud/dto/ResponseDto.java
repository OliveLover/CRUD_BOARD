package com.example.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @param <D>
 * ResponseDto를 wrapping
 */
@Getter
@Setter
@AllArgsConstructor(staticName = "set")                                                          //선언된 모든 필드로 생성자를 자동생성
public class ResponseDto <D> {
    private boolean success;
    private final int statusCode;
    private D data;

    public static <D> ResponseDto setSuccess (D data) {
        return ResponseDto.set(true, 200, data);
    }
}
