package com.example.crud.entity;

import com.example.crud.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne                                                                                               //Member 엔티티와 ManyToOne의 관계를 가져 Member엔티이의 pk를 fk로 사용
   @JoinColumn(name = "user_id")
    private User user;                                                                               //테이블생성시 자동적으로 member_id라는 필드명을 가진다.


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
}
