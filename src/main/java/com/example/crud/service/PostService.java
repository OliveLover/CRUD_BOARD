package com.example.crud.service;

import com.example.crud.dto.PostRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.entity.Post;
import com.example.crud.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor                                                                               //생성자를 자동생성하여 자동주입
public class PostService {
    private final PostRepository postRepository;

    /*
    @Transactional
     - 데이터베이스의 상태를 변경하는 작업 또는 한버에 수행해야하는 연산
     - begin, commit을 자동으로 수행
     - 예외 발생시 rollbackc 처리를 자동으로 수행

     트랜잭션의 4가지 성질
     - 원자성 : 한 트랜잭션 내에서 실행한 작업들은 하나의 단위로 처리한다.
                    모두 성공 또는 모두 실패
     - 일관성 : 일관성 있는 데이터 베이스를 유지 (data integrity 만족)
     - 격리성 : 동시에 실행되는 트랜잭션들이 서로 영향을 미치지 않도록 격리해야한다.
     - 영속성 : 트랜잭션을 성공적으로 마치면 결과가 항상 저장되어야 한다.
     */

    @Transactional
    public ResponseDto<PostRequestDto> createPost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        postRepository.save(post);
        return ResponseDto.setSuccess(post);
    }

    public ResponseDto<List> getPost() {
        List<Post> getPost= postRepository.findAllByOrderByCreatedAtDesc();
        return ResponseDto.setSuccess(getPost);
    }
}
