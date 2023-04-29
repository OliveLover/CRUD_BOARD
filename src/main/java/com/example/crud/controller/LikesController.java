package com.example.crud.controller;

import com.example.crud.dto.ResponseDto;
import com.example.crud.security.UserDetailsImpl;
import com.example.crud.service.LikesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Tag(name = "LikesController", description = "좋아요 Controller")
public class LikesController {

    private final LikesService likesService;

    /*
    @Service에서 게시글 좋아요 메서드 호출
     */

    @Operation(summary = "게시글 좋아요 API", description = "postId(게시글 아이디)를 매개변수로 해당 게시글에 좋아요+1, 이미 눌렀다면 -1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 +1 or 좋아요 -1")
    })
    @PostMapping("like/{postId}")
    public ResponseDto<?> postLikes(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likesService.postLikes(postId, userDetails.getUser());
    }



    /*
    @Service에서 댓글 좋아요 메서드 호출
     */
    @Operation(summary = "댓글 좋아요 API", description = "commentId(댓글 아이디)를 매개변수로 해당 댓글에 좋아요+1, 이미 눌렀다면 -1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 +1 or 좋아요 -1")
    })
    @PostMapping("like/{postId}/{commentId}")
    public ResponseDto<?> commentLikes(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likesService.commentLikes(postId, commentId, userDetails.getUser());
    }
}
