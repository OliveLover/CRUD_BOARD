package com.example.crud.service;

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
        //String password = signUpRequestDto.getPassword();

        //회원 중복 확인
        Optional<Member> found = memberRepository.findByName(name);
        if(found.isPresent()) {
            return ResponseDto.set(false, 409, "이미 존재하는 아이디 입니다.");
        }

        Member member = new Member(signUpRequestDto);
        memberRepository.save(member);

        return ResponseDto.setSuccess("회원 가입이 완료 되었습니다.");
    }
}
