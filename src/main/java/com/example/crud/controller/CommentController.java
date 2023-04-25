package com.example.crud.controller;

import com.example.crud.dto.CommentRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.security.UserDetailsImpl;
import com.example.crud.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class CommentController {
    private final CommentService commentService;

    /*
    @Service에서 댓글 생성 메서드 호출
     */
    @PostMapping("comment/{postId}")
    public ResponseDto<?> createComment(@PathVariable Long postId, @RequestBody  CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequestDto, userDetails);
    }

    /*
    @Service에서 댓글 수정 메서드 호출
     */
    @PutMapping("comment/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, commentRequestDto, userDetails);
    }

    /*
    @Service에서 댓글 삭제 메서드 호출
     */
    @DeleteMapping("comment/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails);
    }
}
