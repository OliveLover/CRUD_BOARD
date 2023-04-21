package com.example.crud.service;

import com.example.crud.dto.LoginRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.dto.SignUpRequestDto;
import com.example.crud.entity.Member;
import com.example.crud.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<?> signUp(SignUpRequestDto signUpRequestDto) {
        String name = signUpRequestDto.getName();

        //회원 중복 확인
        Optional<Member> found = memberRepository.findByName(name);
        if(found.isPresent()) {
            return ResponseDto.set(false, 409, "이미 존재하는 아이디 입니다.");
        }

        Member member = new Member(signUpRequestDto);
        memberRepository.save(member);

        return ResponseDto.setSuccess("회원 가입이 완료 되었습니다.");
    }

    public ResponseDto<?> login(LoginRequestDto loginRequestDto) {
        String name = loginRequestDto.getName();
        String password = loginRequestDto.getPassword();

        //사용자 확인
        Member member = memberRepository.findByName(name).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        //비밀번호 확인
        if(!member.getPassword().equals(password)) {
            return ResponseDto.set(false, 401, "잘못된 사용자 입니다.");
        }

        return ResponseDto.setSuccess("로그인 되었습니다.");

    }
}
