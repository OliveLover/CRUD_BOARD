package com.example.crud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="comment_text")
    private String text;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Post post;
}
