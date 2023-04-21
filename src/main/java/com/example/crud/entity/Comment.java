package com.example.crud.entity;

import com.example.crud.dto.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="comment_text")
    private String text;

    @Column(name ="comment_writer")
    private String writerName;

    @JsonIgnore
    @ManyToOne
    private Member member;

    @JsonIgnore
    @ManyToOne
    private Post post;

    public Comment (CommentRequestDto commentRequestDto, Member member, Post post) {
        this.text = commentRequestDto.getText();
        this.writerName = member.getName();
        this.member = member;
        this.post = post;
    }
}
