package com.example.crud.controller;

import com.example.crud.dto.ResponseDto;
import com.example.crud.security.UserDetailsImpl;
import com.example.crud.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class LikesController {

    private final LikesService likesService;

    /*
    @Service에서 게시글 좋아요 메서드 호출
     */
    @PostMapping("like/{postId}")
    public ResponseDto<?> postLikes(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likesService.postLikes(postId, userDetails.getUser());
    }



    /*
    @Service에서 댓글 좋아요 메서드 호출
     */
    @PostMapping("like/{postId}/{commentId}")
    public ResponseDto<?> commentLikes(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likesService.commentLikes(postId, commentId, userDetails.getUser());
    }
}
