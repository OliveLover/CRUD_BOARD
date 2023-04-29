package com.example.crud.controller;

import com.example.crud.dto.CommentRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.security.UserDetailsImpl;
import com.example.crud.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
@Tag(name = "CommentController", description = "댓글 Controller")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성 API", description = "postId(게시글 아이디) 위치에 댓글 생성")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "댓글 저장 완료")})
    @PostMapping("comment/{postId}")
    public ResponseDto<?> createComment(@PathVariable Long postId, @RequestBody  CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequestDto, userDetails.getUser());
    }

    @Operation(summary = "댓글 수정 API", description = "commentId(댓글아이디)를 매개변수로 해당 댓글 수정")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "댓글 수정 완료")})
    @PutMapping("comment/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, commentRequestDto, userDetails.getUser());
    }

    @Operation(summary = "댓글 삭제 API", description = "commentId(댓글아이디)를 매개변수로 해당 댓글 삭제")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "댓글 삭제 완료")})
    @DeleteMapping("comment/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails.getUser());
    }
}
