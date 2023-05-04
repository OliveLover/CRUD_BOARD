package com.example.crud.entity;

import com.example.crud.dto.UserRole;
import com.example.crud.dto.SignUpRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter                                                                                                            //getter 자동 생성
@Entity(name ="users")                                                                                     //JPA에서 해당 클래스를 Entity로 추적하여 관리
@NoArgsConstructor                                                                                         //기본생성자 자동생성
public class User {
    @Id
    /*
      @GeneratedValue(strategy = GenerationType.AUTO)
      데이터베이스 벤더 제공하는 방식으로 자동으로 primary key를 자동 생성

      @GeneratedValue(startegy = GenerationType.IDENTITY)
      MySQL에서는 AUTO_INCREMENT가 primary key를 자동생성하도록
      지원되지 않기 때문에 IDENTITY를 사용

      GenerationType.AUTO : 자동 생성 방식을 데이터베이스 벤더에게 맡긴다.
      GnerationType.IDENTITY : MySQL에서만 사용
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="user_name", unique = true, length = 30)
    private String name;

    @Column(name ="user_email", nullable = false, length = 50)
    private String email;

    @Column(name ="user_password", nullable = false, length = 150)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> post = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comment = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostLikes> postLike = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommentLikes> commentLike = new ArrayList<>();


    public User(SignUpRequestDto signUpRequestDto, String encode, UserRole userRole) {
        this.name = signUpRequestDto.getName();
        this.email = signUpRequestDto.getEmail();
        this.password = encode;
        this.userRole = userRole;
    }
}
