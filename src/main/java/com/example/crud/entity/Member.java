package com.example.crud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter                                                                                                            //getter 자동 생성
@Entity                                                                                                             //JPA에서 해당 클래스를 Entity로 추적하여 관리
@NoArgsConstructor                                                                                         //기본생성자 자동생성
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="member_id")
    private Long id;

    @Column(name ="member_name", nullable = false)
    private String name;

    @Column(name ="member_email", nullable = false)
    private String email;

    @Column(name ="member_password", nullable = false)
    private String password;
}
