package com.example.crud.service;

import com.example.crud.dto.*;
import com.example.crud.entity.RefreshToken;
import com.example.crud.entity.User;
import com.example.crud.jwt.JwtUtil;
import com.example.crud.repository.RefreshTokenRepository;
import com.example.crud.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private  final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenRepository refreshTokenRepository;

    /*
   회원가입 메서드
    */
    @Transactional
    public ResponseDto<?> signUp(SignUpRequestDto signUpRequestDto) {
        String name = signUpRequestDto.getName();
        String password = signUpRequestDto.getPassword();
        String adminToken = signUpRequestDto.getAdminToken();

        String namePattern = "^[0-9a-z]{4,10}$";
        if (!name.matches(namePattern)) {
            return ResponseDto.set(false, 406, "올바르지 않은 아이디 형식 입니다.");
        }

        String passwordPattern = "^[a-zA-Z0-9`~!@#$%^&*()\\-_=+\\\\|\\[\\]{};:\'\",.<>/?]{0,9}$";
        if (!password.matches(passwordPattern)) {
            return ResponseDto.set(false, 406, "올바르지 않은 비밀번호 형식 입니다.");
        }

        //관리자 토큰의 존재 유무 확인
        if(!adminToken.equals("")) signUpRequestDto.setAdmin(true);

        //회원 중복 확인
        Optional<User> found = userRepository.findByName(name);
        if(found.isPresent()) {
            return ResponseDto.set(false, 409, "이미 존재하는 아이디 입니다.");
        }

        //관리자 권한 확인
        UserRole userRole = UserRole.USER;
        if(signUpRequestDto.isAdmin()) {
            if(!signUpRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("유효하지 않은 코드입니다.");
            }
            userRole = UserRole.ADMIN;
        }

        User user = new User(signUpRequestDto,  passwordEncoder.encode(password) ,userRole);
        userRepository.save(user);

        return ResponseDto.setSuccess("회원 가입이 완료 되었습니다.");
    }

    /*
   로그인 메서드
    */
    @Transactional
    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        String name = loginRequestDto.getName();
        String password = loginRequestDto.getPassword();

        //사용자 확인
        User user = userRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        //비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseDto.set(false, 401, "아이디 또는 비밀번호를 다시 확인해주세요.");
        }

        //아이디 정보로 Token 생성
        TokenDto tokenDto = jwtUtil.createAllToken(loginRequestDto.getName());

        //Refresh Token 이 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsername(loginRequestDto.getName());

        /*
        Refresh Token이 있다면 발급 후 업데이트
        Refresh Token이 없다면 새로 생성한 후 DB에 저장
         */
        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginRequestDto.getName());
            refreshTokenRepository.save(newToken);
        }

        //httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getName(), user.getUserRole()));
        setHeader(httpServletResponse, tokenDto);
        return ResponseDto.setSuccess("로그인 되었습니다.");
    }

    public void setHeader(HttpServletResponse httpServletResponse, TokenDto tokenDto) {
        httpServletResponse.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        httpServletResponse.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

}
