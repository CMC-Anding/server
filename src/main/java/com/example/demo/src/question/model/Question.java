package com.example.demo.src.question.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Question {
    private int userIdx;
    private String ID;
    private String userName;
    private String password;
    private String email;
}
