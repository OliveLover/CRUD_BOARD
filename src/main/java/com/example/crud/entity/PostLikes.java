package com.example.crud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class PostLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

 //   @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public PostLikes (User user, Post post){
        this.user = user;
        this.post = post;
    }

}
