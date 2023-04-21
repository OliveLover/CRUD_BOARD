package com.example.crud.service;

import com.example.crud.dto.PostRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.entity.Post;
import com.example.crud.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor                                                                               //생성자를 자동생성하여 자동주입
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public ResponseDto<?> createPost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        postRepository.save(post);
        return ResponseDto.setSuccess(post);
    }
}
