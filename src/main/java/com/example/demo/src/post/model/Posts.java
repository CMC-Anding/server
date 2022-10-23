package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Posts {
    private int userId;
    private String contents;
    private String dailyTitle;
    private String qnaBackgroundColor;
    private String filterId;
    private String qnaQuestionId;
    private String qnaQuestionMadeFromUser;
    private String feedShare;
}