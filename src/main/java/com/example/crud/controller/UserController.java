package com.example.crud.controller;

import com.example.crud.dto.LoginRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.dto.SignUpRequestDto;
import com.example.crud.security.UserDetailsImpl;
import com.example.crud.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
@Tag(name = "UserController", description = "유저 Controller")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입 API", description = "회원가입")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "회원 가입 완료")})
    @PostMapping("signup")
    public ResponseDto<?> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return userService.signUp(signUpRequestDto);
    }

    @Operation(summary = "로그인 API", description = "로그인, RefreshToken, AccessToken")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "회원 가입 완료")})
    @PostMapping("login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        return userService.login(loginRequestDto, httpServletResponse);
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "회원 가입 완료")})
    @PostMapping("logout")
    public ResponseDto<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse httpServletResponse) {
        return userService.logout(userDetails.getUser(), httpServletResponse);
    }

    @Operation(summary = "회원 탈퇴 API", description = "회원탈퇴")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "회원 탈퇴 완료")})
    @PostMapping("signout")
    public ResponseDto<?> signOut(@RequestBody LoginRequestDto loginRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse httpServletResponse) {
        return userService.signOut(loginRequestDto, userDetails.getUser(), httpServletResponse);
    }
}
