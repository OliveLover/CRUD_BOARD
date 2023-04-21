package com.example.crud.controller;

import com.example.crud.dto.PostRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        return postService.createPost(postRequestDto, httpServletRequest);
    }

    @GetMapping("posts")
    public ResponseDto<?> getPost() {
        return postService.getPost();
    }

    @GetMapping("post/{postId}")
    public ResponseDto<?> getSelectPost(@PathVariable Long postId) {
        return postService.getSelectPost(postId);
    }

    @PutMapping("post/{postId}")
    public ResponseDto<?> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        return postService.updatePost(postId, postRequestDto, httpServletRequest);
    }

    @DeleteMapping("post/{postId}")
    public ResponseDto<?> deletePost(@PathVariable Long postId, HttpServletRequest httpServletRequest) {
        return postService.deletePost(postId, httpServletRequest);
    }

}
