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

    /*
    게시글 좋아요 메서드
     */
    @Transactional
    public ResponseDto<?> postLikes(Long postId, UserDetailsImpl userDetails) {
        Post post = postCheck(postId);

        Optional<PostLikes> likesCheck = postLikesRepository.findByUserIdAndPostId(userDetails.getUser().getId(), postId);

//test 해봐야할 코드
//        likesCheck.ifPresent(postLikes ->{postLikes.getPost().deCntLike();
//        postLikesRepository.delete(likesCheck.get());
//        });  //null이아니면 뒤에것을 수행

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

    /*
   댓글 좋아요 메서드
    */
    @Transactional
    public ResponseDto<?> commentLikes(Long postId, Long commentId, UserDetailsImpl userDetails) {
        Post post = postCheck(postId);

        Comment comment = commentCheck(commentId);

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

    /*
    댓글 존재 유무 체크
     */
    public Comment commentCheck(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글 입니다.")
        );
    }

    /*
    게시글 존재 유무 체크
     */
    public Post postCheck(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물 입니다.")
        );
    }
}
