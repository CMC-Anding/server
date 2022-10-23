package com.example.demo.src.question.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetQuestionRes {
    private String contents;
    private int numberOfRemaning;
}
