package com.example.crud.controller;

import com.example.crud.dto.CommentRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.security.UserDetailsImpl;
import com.example.crud.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("comment/{postId}")
    public ResponseDto<?> createComment(@PathVariable Long postId, @RequestBody  CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequestDto, userDetails);
    }

    @PutMapping("comment/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, commentRequestDto, userDetails);
    }

    @DeleteMapping("comment/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails);
    }
}
