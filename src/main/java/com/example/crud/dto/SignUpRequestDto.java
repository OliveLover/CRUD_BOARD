package com.example.crud.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

    private String name;
    private String email;
    private String password;

    private boolean admin = false;
    private String adminToken = "";
}
