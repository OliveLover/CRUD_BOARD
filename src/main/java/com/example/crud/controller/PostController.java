package com.example.crud.controller;

import com.example.crud.dto.PostRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.security.UserDetailsImpl;
import com.example.crud.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "PostController", description = "게시글 Controller")
public class PostController {
    private final PostService postService;

    /*
   @Service에서 게시글 작성 메서드 호출
    */
    @Operation(summary = "게시글 생성 API", description = "게시글을 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 저장 완료")
    })
    @PostMapping("post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails.getUser());
    }

    /*
   @Service에서 게시글 전체 목록 조회 메서드 호출
    */
    @Operation(summary = "전체 게시글 조회 API", description = "전체 게시글 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 게시글 목록 출력")
    })
    @GetMapping("posts")
    public ResponseDto<?> getPost() {
        return postService.getPost();
    }

    /*
   @Service에서 지정한 게시글 조회 메서드 호출
    */
    @Operation(summary = "선택한 게시글 조회 API", description = "선택한 게시글 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선택한 게시글을 출력")
    })
    @GetMapping("post/{postId}")
    public ResponseDto<?> getSelectPost(@PathVariable Long postId) {
        return postService.getSelectPost(postId);
    }

    /*
  @Service에서 게시글 수정 메서드 호출
   */
    @Operation(summary = "선택한 게시글 수정 API", description = "선택한 게시글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선택한 게시글을 수정")
    })
    @PutMapping("post/{postId}")
    public ResponseDto<?> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(postId, postRequestDto, userDetails.getUser());
    }

    /*
  @Service에서 게시글 삭제 메서드 호출
   */
    @Operation(summary = "선택한 게시글 삭제 API", description = "선택한 게시글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선택한 게시글을 삭제")
    })
    @DeleteMapping("post/{postId}")
    public ResponseDto<?> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(postId, userDetails.getUser());
    }

}
