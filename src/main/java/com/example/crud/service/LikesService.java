package com.example.crud.service;

import com.example.crud.dto.ResponseDto;
import com.example.crud.entity.Comment;
import com.example.crud.entity.Likes;
import com.example.crud.entity.Post;
import com.example.crud.repository.CommentRepository;
import com.example.crud.repository.LikesRepository;
import com.example.crud.repository.PostRepository;
import com.example.crud.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    public ResponseDto<?> postLikes(Long postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        //Likes likes = likesRepository.save(post);

        post.cntLike();
        Likes likes = new Likes(userDetails, post);
        likesRepository.save(likes);

        return ResponseDto.setSuccess(post.getLikesNum());
    }

    public ResponseDto<?> commentLikes(Long postId, Long commentId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        comment.cntLike();

        Likes likes = new Likes(userDetails, post, comment);
        likesRepository.save(likes);
        return ResponseDto.setSuccess(comment.getLikesNum());
    }
}
