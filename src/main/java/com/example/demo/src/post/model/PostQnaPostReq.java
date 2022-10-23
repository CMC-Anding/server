package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostQnaPostReq {
    private int userId;
    private String filterId;
    private String qnaQuestionId;
    private String contents;
    private String qnaBackgroundColor;
    private String qnaQuestionMadeFromUser;
    private String feedShare;
}