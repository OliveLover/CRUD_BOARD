package com.example.crud.service;

import com.example.crud.dto.ResponseDto;
import com.example.crud.entity.Comment;
import com.example.crud.entity.CommentLikes;
import com.example.crud.entity.PostLikes;
import com.example.crud.entity.Post;
import com.example.crud.repository.CommentLikesRepository;
import com.example.crud.repository.CommentRepository;
import com.example.crud.repository.PostLikesRepository;
import com.example.crud.repository.PostRepository;
import com.example.crud.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final PostLikesRepository postLikesRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final CommentLikesRepository commentLikesRepository;

    @Transactional
    public ResponseDto<?> postLikes(Long postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        Optional<PostLikes> likesCheck = postLikesRepository.findByUserIdAndPostId(userDetails.getUser().getId(), postId);

        if(likesCheck.isPresent()){
            post.deCntLike();
            postLikesRepository.delete(likesCheck.get());
        } else {
            post.cntLike();
            PostLikes postLikes = new PostLikes(userDetails, post);
            postLikesRepository.save(postLikes);
        }

        return ResponseDto.setSuccess(post.getLikesNum());
    }

    @Transactional
    public ResponseDto<?> commentLikes(Long postId, Long commentId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        Optional<CommentLikes> likesCheck = commentLikesRepository.findByUserIdAndCommentId(userDetails.getUser().getId(), commentId);

        if(likesCheck.isPresent()){
            comment.deCntLike();
            commentLikesRepository.delete(likesCheck.get());
        } else {
            comment.cntLike();
            CommentLikes commentLikes = new CommentLikes(userDetails, comment);
            commentLikesRepository.save(commentLikes);
        }

        return ResponseDto.setSuccess(comment.getLikesNum());
    }
}
