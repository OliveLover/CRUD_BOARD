package com.example.crud.service;

import com.example.crud.dto.ResponseDto;
import com.example.crud.entity.*;
import com.example.crud.repository.CommentLikesRepository;
import com.example.crud.repository.CommentRepository;
import com.example.crud.repository.PostLikesRepository;
import com.example.crud.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final PostLikesRepository postLikesRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final CommentLikesRepository commentLikesRepository;

    @Transactional
    public ResponseDto<?> postLikes(Long postId, User user) {
        Post post = postCheck(postId);

        Optional<PostLikes> likesCheck = postLikesRepository.findByUserIdAndPostId(user.getId(), postId);

//test 해봐야할 코드
//        likesCheck.ifPresent(postLikes ->{postLikes.getPost().deCntLike();
//        postLikesRepository.delete(likesCheck.get());
//        });  //null이아니면 뒤에것을 수행

        if(likesCheck.isPresent()){
            postLikesRepository.delete(likesCheck.get());
        } else {
            PostLikes postLikes = new PostLikes(user, post);

            postLikesRepository.save(postLikes);
        }

        long likes = postLikesRepository.countByPostId(postId);
        post.checkLikes(likes);
        return ResponseDto.setSuccess(likes);
    }

    @Transactional
    public ResponseDto<?> commentLikes(Long postId, Long commentId, User user) {
        Post post = postCheck(postId);

        Comment comment = commentCheck(commentId);

        Optional<CommentLikes> likesCheck = commentLikesRepository.findByUserIdAndCommentId(user.getId(), commentId);

        if(likesCheck.isPresent()){
            commentLikesRepository.delete(likesCheck.get());
        } else {
            CommentLikes commentLikes = new CommentLikes(user, comment);
            commentLikesRepository.save(commentLikes);
        }

        long likes = commentLikesRepository.countByCommentId(commentId);

        comment.checkLikes(likes);

        return ResponseDto.setSuccess(likes);
    }

    public Comment commentCheck(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글 입니다.")
        );
    }

    public Post postCheck(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물 입니다.")
        );
    }
}
