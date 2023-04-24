package com.example.crud.entity;

import com.example.crud.security.UserDetailsImpl;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Likes (UserDetailsImpl userDetails, Post post){
        this.user = userDetails.getUser();
        this.post = post;
        this.comment = null;
    }

    public Likes (UserDetailsImpl userDetails, Post post, Comment comment){
        this.user = userDetails.getUser();
        this.post = post;
        this.comment = comment;
    }
}
