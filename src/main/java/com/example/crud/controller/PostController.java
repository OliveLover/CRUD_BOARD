package com.example.crud.controller;

import com.example.crud.dto.PostRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.security.UserDetailsImpl;
import com.example.crud.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
/*
  @RestController = @Controller + @ResponseBody
  Json형태로 객체 데이터를 반환

  @Controller : Spring MVC의 컨트롤러
  View를 반환

 @ResponseBody : 클라이언트에서 서버로 응답데이터를 보낼때 Json형태로 처리
 */
@RequestMapping("api")
/*
 Been 중의 하나인 RequestMappingHandlerMapping에서
 @RequestMapping으로 등록한 메서들을 가지고 있다가  요청이 들어오면 매핑해준다.
 */
public class PostController {
    private final PostService postService;

    /*
   @Service에서 게시글 작성 메서드 호출
    */
    @PostMapping("post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails);
    }

    /*
   @Service에서 게시글 전체 목록 조회 메서드 호출
    */
    @GetMapping("posts")
    public ResponseDto<?> getPost() {
        return postService.getPost();
    }

    /*
   @Service에서 지정한 게시글 조회 메서드 호출
    */
    @GetMapping("post/{postId}")
    public ResponseDto<?> getSelectPost(@PathVariable Long postId) {
        return postService.getSelectPost(postId);
    }

    /*
  @Service에서 게시글 수정 메서드 호출
   */
    @PutMapping("post/{postId}")
    public ResponseDto<?> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(postId, postRequestDto, userDetails);
    }

    /*
  @Service에서 게시글 삭제 메서드 호출
   */
    @DeleteMapping("post/{postId}")
    public ResponseDto<?> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(postId, userDetails);
    }

}
