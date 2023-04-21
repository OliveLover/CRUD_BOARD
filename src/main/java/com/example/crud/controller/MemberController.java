package com.example.crud.controller;

import com.example.crud.dto.LoginRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.dto.SignUpRequestDto;
import com.example.crud.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("signup")
    public ResponseDto<?> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return memberService.signUp(signUpRequestDto);
    }

    @PostMapping("login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        return memberService.login(loginRequestDto, httpServletResponse);
    }
}
