package com.example.demo.src.post.model;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPostDetailRes {
    private String nickname;
    private String contents;
    private String dailyTitle;
    private String qnaBackgroundColor;
    private String filterId;
    private String qnaQuestionId; //추후 질문별 이미지 다르게 할 경우 대비
    private String qnaQuestion;
    private String dailyImage;
    private String qnaQuestionMadeFromUser;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
}
