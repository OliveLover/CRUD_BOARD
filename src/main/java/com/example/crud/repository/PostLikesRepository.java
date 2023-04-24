package com.example.crud.repository;

import com.example.crud.entity.CommentLikes;
import com.example.crud.entity.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

    Optional<PostLikes> findByUserIdAndPostId(Long userId, Long postId);

//    Optional<CommentLikes> findByUserIdAndCommentId(Long userId, Long commentId);
//    Optional<PostLikes> findByUserIdAndPostIdAndCommentId(Long userId, Long postId, Long commentId);


    //@Query(value = "select '*' from Likes l where l.comment = {}")
//    void deleteByUserIdAndPostId(Long userId, Long postId);
//    void deleteByUserIdAndPostIdAndCommentId(Long userId, Long postId, Long commentId);

}
