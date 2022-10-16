package com.example.demo.src.post.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPostReq {
    private int userId;
    private String contents;
    private String daily_title;
    private String qnaBackgroundColor;
    private String filterId;
    private String qnaQuestionId;
    private String questionMadeFromUser;
}
