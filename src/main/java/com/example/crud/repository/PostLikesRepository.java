package com.example.crud.repository;

import com.example.crud.entity.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
    Optional<PostLikes> findByUserIdAndPostId(Long userId, Long postId);

    Long countByUserIdAndPostId(Long userId, Long postId);
}
