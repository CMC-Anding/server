package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int id; //테이블 인덱스 값
    private String userId;
    private String password;
    private String nickname;
    private String phone;
}
