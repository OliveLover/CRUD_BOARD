package com.example.crud.entity;

import com.example.crud.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

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

    @Column
    @ColumnDefault("0")
    private long likesNum;

    ///@JsonIgnore
    @ManyToOne
    private User user;

    //@JsonIgnore
    @ManyToOne
    private Post post;

//    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
//    private List<CommentLikes> commentLikesList = new ArrayList<>();

    public Comment (CommentRequestDto commentRequestDto, User user, Post post) {
        this.text = commentRequestDto.getText();
        this.writerName = user.getName();
        this.user = user;
        this.post = post;
    }

    public void updateComment(CommentRequestDto commentRequestDto) {
        this.text = commentRequestDto.getText();
    }

    public void checkLikes(long likes){
        this.likesNum = likes;
    }

}
