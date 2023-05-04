package com.example.crud.controller;

import com.example.crud.dto.PostRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.exception.CustomException;
import com.example.crud.exception.ErrorCode;
import com.example.crud.security.UserDetailsImpl;
import com.example.crud.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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

    @Operation(summary = "게시글 생성 API", description = "게시글을 생성")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "게시글 저장 완료"),
    @ApiResponse(responseCode = "403", description = "로그인이 필요 합니다.")})
    @PostMapping("post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails == null) {
            throw new CustomException(ErrorCode.NEED_TO_LOGIN);
        }
        return postService.createPost(postRequestDto, userDetails.getUser());
    }

    @Operation(summary = "전체 게시글 조회 API", description = "전체 게시글 조회")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "전체 게시글 목록 출력")})
    @GetMapping("posts")
    public ResponseDto<?> getPost(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc) {
        return postService.getPost(page-1, size, sortBy, isAsc);
    }

    @Operation(summary = "선택한 게시글 조회 API", description = "선택한 게시글 조회")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "선택한 게시글을 출력")})
    @GetMapping("post/{postId}")
    public ResponseDto<?> getSelectPost(@PathVariable Long postId) {
        return postService.getSelectPost(postId);
    }

    @Operation(summary = "선택한 게시글 수정 API", description = "선택한 게시글 수정")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "선택한 게시글을 수정")})
    @PutMapping("post/{postId}")
    public ResponseDto<?> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(postId, postRequestDto, userDetails.getUser());
    }

    @Operation(summary = "선택한 게시글 삭제 API", description = "선택한 게시글 삭제")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "선택한 게시글을 삭제")})
    @DeleteMapping("post/{postId}")
    public ResponseDto<?> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(postId, userDetails.getUser());
    }

}
