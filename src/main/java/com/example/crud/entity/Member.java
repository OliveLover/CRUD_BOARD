package com.example.crud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter                                                                                                            //getter 자동 생성
@Entity                                                                                                             //JPA에서 해당 클래스를 Entity로 추적하여 관리
@NoArgsConstructor                                                                                         //기본생성자 자동생성
public class Member {
    @Id                                                                                                              //member_id를 pk로 설정
    /*
      @GeneratedValue(strategy = GenerationType.AUTO)
      데이터베이스 벤더 제공하는 방식으로 자동으로 primary key를 자동 생성

      @GeneratedValue(startegy = GenerationType.IDENTITY)
      MySQL에서는 AUTO_INCREMENT가 primary key를 자동생성하도록
      지원되지 않기 때문에 IDENTITY를 사용

      GenerationType.AUTO : 자동 생성 방식을 데이터베이스 벤더에게 맡긴다.
      GnerationType.IDENTITY : MySQL에서만 사용
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)                             //id값을 null로 지정할 경우 자동적으로 Auto_Increment
    private Long id;

    @Column(name ="member_name", unique = true, length = 30)                         //entity클래스의 컬럼명을 데이터베이스에 매핑, 길이제한
    private String name;

    @Column(name ="member_email", nullable = false, length = 30)
    private String email;

    @Column(name ="member_password", nullable = false, length = 50)
    private String password;
}
