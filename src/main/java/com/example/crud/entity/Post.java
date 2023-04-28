package com.example.crud.entity;

import com.example.crud.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="user_name", length = 30)
    private String name;

    @Column(name ="post_title", length = 100)
    private String title;

    @Column(name ="post_contents")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @ColumnDefault("0")
    private long likesNum;

    @OneToMany(mappedBy = "post")
    @OrderBy("createdAt DESC")
    private List<Comment> commentList = new ArrayList<>();

    public Post (PostRequestDto postRequestDto, User user) {
        this.user = user;
        this.name = user.getName();
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
        comment.setPost(this);
    }

    public void checkLikes(long likes){
        this.likesNum = likes;
    }
}
