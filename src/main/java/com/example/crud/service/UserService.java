package com.example.crud.service;

import com.example.crud.dto.LoginRequestDto;
import com.example.crud.dto.UserRole;
import com.example.crud.dto.ResponseDto;
import com.example.crud.dto.SignUpRequestDto;
import com.example.crud.entity.User;
import com.example.crud.jwt.JwtUtil;
import com.example.crud.repository.UserRepository;
import com.example.crud.security.UserDetailsImpl;
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

    /*
   회원가입 메서드
    */
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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

        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getName(), user.getUserRole()));

        return ResponseDto.setSuccess("로그인 되었습니다.");
    }

}
