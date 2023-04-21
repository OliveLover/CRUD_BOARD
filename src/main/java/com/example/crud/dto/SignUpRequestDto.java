package com.example.crud.dto;

import lombok.Getter;

@Getter
public class SignUpRequestDto {

    //@NotNull
    ///@Size(min = 4, max = 10)
    //@Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String name;
    private String email;
    //@NotNull
   //@Size(min = 8, max = 15)
    //@Pattern(regexp = "^[0-9a-zA-Z\\\\d`~!@#$%^&*()-_=+]{8,15}$")
    private String password;
}
