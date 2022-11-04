package com.example.demo.src.archive.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class GetArchiveQnaRes {
    private int postId;
    private String qnaBackgroundColor;
    private String filterId; //전체필터에서 필터별 이미지 분류 출력을 위함
    private String qnaQuestionId; //추후 질문별 이미지 다르게 할 경우 대비
    private String qnaQuestion; 
    private String qnaQuestionMadeFromUser;  
}
