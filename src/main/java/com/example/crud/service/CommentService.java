package com.example.crud.service;

import com.example.crud.dto.CommentRequestDto;
import com.example.crud.dto.ResponseDto;
import com.example.crud.entity.Comment;
import com.example.crud.entity.Post;
import com.example.crud.entity.User;
import com.example.crud.repository.CommentRepository;
import com.example.crud.repository.PostRepository;
import com.example.crud.repository.UserRepository;
import com.example.crud.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /*
    댓글 생성 메서드
     */
    @Transactional
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseDto<?> createComment(Long postId, CommentRequestDto commentRequestDto, User user) {

        Post post = postCheck(postId);

        Comment comment = new Comment(commentRequestDto, user, post);

        post.addComment(comment);
        comment.setUser(user);

        commentRepository.save(comment);
        return ResponseDto.setSuccess(comment);
    }

    /*
    댓글 수정 메서드
     */
    @Transactional
    public ResponseDto<?> updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = commentCheck(commentId);

        /************관리자 권한 *********************/
        if(user.getUserRole() == user.getUserRole().ADMIN) {
            comment.updateComment(commentRequestDto);
            return ResponseDto.setSuccess("관리자의 권한으로 수정 되었습니다.");
        }
        /*****************************************/

        if(comment.getUser() != user) {
            return ResponseDto.set(false, 403, "수정할 권한이 없음");
        }

        comment.updateComment(commentRequestDto);
        return ResponseDto.setSuccess(comment);
    }

    /*
    댓글 삭제 메서드
     */
    @Transactional
    public ResponseDto<?> deleteComment(Long commentId, User user) {
        Comment comment = commentCheck(commentId);

        /************관리자 권한 *********************/
        if(user.getUserRole() == user.getUserRole().ADMIN) {
            commentRepository.deleteById(commentId);
            return ResponseDto.setSuccess("관리자에의해 수정된 게시글입니다.");
        }
        /*****************************************/

        if(comment.getUser() != user) {
            return ResponseDto.set(false, 403, "삭제할 권한이 없음");
        }

        commentRepository.deleteById(commentId);
        return ResponseDto.setSuccess("삭제 되었습니다.");
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
