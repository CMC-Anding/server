package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPostDetailRes {
    private String contents;
    private String dailyTitle;
    private String qnaBackgroundColor;
    private String filterId;
    private String qnaQuestionId;
    private String qnaQuestion;
    private String dailyImage;
    private String qnaQuestionMadeFromUser;
}
