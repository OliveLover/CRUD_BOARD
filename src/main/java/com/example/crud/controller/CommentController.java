package com.example.crud.controller;

import com.example.crud.dto.CommentRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("comment/{postId}")
    public ResponseDto<?> createComment(@PathVariable Long postId, @RequestBody  CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        return commentService.createComment(postId, commentRequestDto, httpServletRequest);
    }

    @PutMapping("comment/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        return commentService.updateComment(commentId, commentRequestDto, httpServletRequest);
    }

    @DeleteMapping("comment/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId, HttpServletRequest httpServletRequest) {
        return commentService.deleteComment(commentId, httpServletRequest);
    }
}
