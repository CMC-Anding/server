package com.example.demo.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetFeedListRes {
    private int postId;
    private String dailyTitle;
    private String qnaBackgroundColor;
    private String filterId; //전체필터에서 필터별 이미지 분류 출력을 위함
    private String qnaQuestionId; //추후 질문별 이미지 다르게 할 경우 대비
    private String qnaQuestion; 
    private String dailyImage;
    private String qnaQuestionMadeFromUser;
}
