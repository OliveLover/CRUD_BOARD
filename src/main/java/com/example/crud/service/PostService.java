package com.example.crud.service;

import com.example.crud.dto.MemberRole;
import com.example.crud.dto.PostRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.entity.Member;
import com.example.crud.entity.Post;
import com.example.crud.jwt.JwtUtil;
import com.example.crud.repository.MemberRepository;
import com.example.crud.repository.PostRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor                                                                               //생성자를 자동생성하여 자동주입
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    /*
    @Transactional
     - 데이터베이스의 상태를 변경하는 작업 또는 한버에 수행해야하는 연산
     - begin, commit을 자동으로 수행
     - 예외 발생시 rollbackc 처리를 자동으로 수행

     트랜잭션의 4가지 성질
     - 원자성 : 한 트랜잭션 내에서 실행한 작업들은 하나의 단위로 처리한다.
                    모두 성공 또는 모두 실패
     - 일관성 : 일관성 있는 데이터 베이스를 유지 (data integrity 만족)
     - 격리성 : 동시에 실행되는 트랜잭션들이 서로 영향을 미치지 않도록 격리해야한다.
     - 영속성 : 트랜잭션을 성공적으로 마치면 결과가 항상 저장되어야 한다.
     */

    @Transactional
    public ResponseDto<?> createPost(PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if(token == null) return ResponseDto.set(false, 401, "로그인을 시도 하십시오.");

        if(jwtUtil.validateToken(token)) {
            //토큰에서 사용자 정보 가져오기
            claims = jwtUtil.getUserInfoFromToken(token);
        } else return ResponseDto.set(false, 403, "잘못된 접근입니다.");

        //토큰에서 가져온 사용자 정보를 DB에서 조회
        Member member = memberRepository.findByName(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );

        Post post = new Post(postRequestDto, member);
        postRepository.save(post);
        return ResponseDto.setSuccess(post);
    }

    public ResponseDto<?> getPost() {
        List<Post> getPost= postRepository.findAllByOrderByCreatedAtDesc();
        return ResponseDto.setSuccess(getPost);
    }

    public ResponseDto<?> getSelectPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        return ResponseDto.setSuccess(post);
    }

    @Transactional
    public ResponseDto<?> updatePost(Long postId, PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if(token == null) return ResponseDto.set(false, 401, "로그인을 시도 하십시오.");

        if(jwtUtil.validateToken(token)) {
            //토큰에서 사용자 정보 가져오기
            claims = jwtUtil.getUserInfoFromToken(token);
        } else return ResponseDto.set(false, 403, "잘못된 접근입니다.");

        //토큰에서 가져온 사용자 정보를 DB에서 조회
        Member member = memberRepository.findByName(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );

        /************관리자 권한 *********************/
        if(member.getMemberRole() == MemberRole.ADMIN) {
            post.update(postRequestDto);
            return ResponseDto.setSuccess(post);
        }
        /*****************************************/

        if(post.getMember().getName().equals(claims.getSubject())) {
            post.update(postRequestDto);
            return ResponseDto.setSuccess(post);
        } else {
            return ResponseDto.set(false, 403, "잘못된 요청입니다.");
        }

    }

    @Transactional
    public ResponseDto<?> deletePost(Long postId, HttpServletRequest httpServletRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if(token == null) return ResponseDto.set(false, 401, "로그인을 시도 하십시오.");

        if(jwtUtil.validateToken(token)) {
            //토큰에서 사용자 정보 가져오기
            claims = jwtUtil.getUserInfoFromToken(token);
        } else return ResponseDto.set(false, 403, "잘못된 접근입니다.");

        //토큰에서 가져온 사용자 정보를 DB에서 조회
        Member member = memberRepository.findByName(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );

        /************관리자 권한 *********************/
        if(member.getMemberRole() == MemberRole.ADMIN) {
            postRepository.deleteById(postId);
            return ResponseDto.setSuccess(post);
        }
        /*****************************************/

        if(post.getMember().getName().equals(claims.getSubject())) {
            postRepository.deleteById(postId);
            return ResponseDto.setSuccess("해당 게시글을 삭제 하였습니다.");
        } else {
            return ResponseDto.set(false, 403, "잘못된 요청입니다.");
        }
    }
}
