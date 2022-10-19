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
    private String qnaQuestionId; //추후 질문별 이미지 다르게 할 경우 대비
    private String qnaQuestion;
    private String dailyImage;
    private String qnaQuestionMadeFromUser;
}
