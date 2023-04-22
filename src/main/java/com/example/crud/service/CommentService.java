package com.example.crud.service;

import com.example.crud.dto.CommentRequestDto;
import com.example.crud.dto.MemberRole;
import com.example.crud.dto.ResponseDto;
import com.example.crud.entity.Comment;
import com.example.crud.entity.Member;
import com.example.crud.entity.Post;
import com.example.crud.jwt.JwtUtil;
import com.example.crud.repository.CommentRepository;
import com.example.crud.repository.MemberRepository;
import com.example.crud.repository.PostRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseDto<?> createComment(Long postId, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token == null) return ResponseDto.set(false, 401, "로그인을 하십시오.");

        if (jwtUtil.validateToken(token)) {
            //토큰에서 사용자 정보 가져오기
            claims = jwtUtil.getUserInfoFromToken(token);
        } else return ResponseDto.set(false, 403, "잘못된 접근입니다.");

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물 입니다.")
        );

        //토큰에서 가져온 사용자 정보를 DB에서 조회
        Member member = memberRepository.findByName(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );

        Comment comment = new Comment(commentRequestDto, member, post);

        post.addComment(comment);
        comment.setMember(member);

        commentRepository.save(comment);
        return ResponseDto.setSuccess(comment);
    }

    @Transactional
    public ResponseDto<?> updateComment(Long commentId, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token == null) return ResponseDto.set(false, 401, "로그인을 하십시오.");

        if (jwtUtil.validateToken(token)) {
            //토큰에서 사용자 정보 가져오기
            claims = jwtUtil.getUserInfoFromToken(token);
        } else return ResponseDto.set(false, 403, "잘못된 접근입니다.");

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물 입니다.")
        );

        //토큰에서 가져온 사용자 정보를 DB에서 조회
        Member member = memberRepository.findByName(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );

        /************관리자 권한 *********************/
        if(member.getMemberRole() == member.getMemberRole().ADMIN) {
            comment.updateComment(commentRequestDto);
            return ResponseDto.setSuccess("관리자의 권한으로 수정 되었습니다.");
        }
        /*****************************************/

        if(comment.getMember() != member) {
            return ResponseDto.set(false, 403, "수정할 권한이 없음");
        }

        comment.updateComment(commentRequestDto);
        return ResponseDto.setSuccess(comment);
    }

    @Transactional
    public ResponseDto<?> deleteComment(Long commentId, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.resolveToken(httpServletRequest);
        Claims claims;

        if (token == null) return ResponseDto.set(false, 401, "로그인을 하십시오.");

        if (jwtUtil.validateToken(token)) {
            //토큰에서 사용자 정보 가져오기
            claims = jwtUtil.getUserInfoFromToken(token);
        } else return ResponseDto.set(false, 403, "잘못된 접근입니다.");

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물 입니다.")
        );

        //토큰에서 가져온 사용자 정보를 DB에서 조회
        Member member = memberRepository.findByName(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자 입니다.")
        );

        /************관리자 권한 *********************/
        if(member.getMemberRole() == member.getMemberRole().ADMIN) {
            commentRepository.deleteById(commentId);
            return ResponseDto.setSuccess("관리자에의해 수정된 게시글입니다.");
        }
        /*****************************************/

        if(comment.getMember() != member) {
            return ResponseDto.set(false, 403, "삭제할 권한이 없음");
        }

        commentRepository.deleteById(commentId);
        return ResponseDto.setSuccess("삭제 되었습니다.");
    }
}
