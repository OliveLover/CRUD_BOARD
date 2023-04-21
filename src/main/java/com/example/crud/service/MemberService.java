package com.example.crud.service;

import com.example.crud.dto.LoginRequestDto;
import com.example.crud.dto.MemberRole;
import com.example.crud.dto.ResponseDto;
import com.example.crud.dto.SignUpRequestDto;
import com.example.crud.entity.Member;
import com.example.crud.jwt.JwtUtil;
import com.example.crud.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private  final JwtUtil jwtUtil;

    @Transactional
    public ResponseDto<?> signUp(SignUpRequestDto signUpRequestDto) {
        String name = signUpRequestDto.getName();
        String adminToken = signUpRequestDto.getAdminToken();

        //관리자 토큰의 존재 유무 확인
        if(!adminToken.equals("")) signUpRequestDto.setAdmin(true);

        //회원 중복 확인
        Optional<Member> found = memberRepository.findByName(name);
        if(found.isPresent()) {
            return ResponseDto.set(false, 409, "이미 존재하는 아이디 입니다.");
        }

        //관리자 권한 확인
        MemberRole memberRole = MemberRole.USER;
        if(signUpRequestDto.isAdmin()) {
            if(!signUpRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("유효하지 않은 코드입니다.");
            }
            memberRole = MemberRole.ADMIN;
        }

        Member member = new Member(signUpRequestDto, memberRole);
        memberRepository.save(member);

        return ResponseDto.setSuccess("회원 가입이 완료 되었습니다.");
    }

    public ResponseDto<?> login(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        String name = loginRequestDto.getName();
        String password = loginRequestDto.getPassword();

        //사용자 확인
        Member member = memberRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        //비밀번호 확인
        if(!member.getPassword().equals(password)) {
            return ResponseDto.set(false, 401, "아이디 또는 비밀번호를 다시 확인해주세요.");
        }

        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getName(), member.getMemberRole()));

        return ResponseDto.setSuccess("로그인 되었습니다.");

    }
}
