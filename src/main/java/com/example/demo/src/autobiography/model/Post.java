package com.example.demo.src.autobiography.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class Post {

    @ApiModelProperty(value = "게시글 ID", example ="10", required = true, dataType = "int")
    private int postId;

    @ApiModelProperty(value = "필터 ID", example ="d", required = true, dataType = "String")
    private String filterId;

    @ApiModelProperty(value = "문답 배경 색상", example ="kCGColorSpaceModelRGB 0.943665 0.607611 0.456563 1", required = false, dataType = "String")
    private String qnaBackgroundColor;

    @ApiModelProperty(value = "문답 ID", example ="d-1", required = false, dataType = "String")
    private String qnaQuestionId; //추후 질문별 이미지 다르게 할 경우 대비

    @ApiModelProperty(value = "문답 질문", example ="언젠간 꼭 사고 싶은 물건이 있나요?", required = false, dataType = "String")
    private String qnaQuestion;

    @ApiModelProperty(value = "사용자 작성 문답 질문", example ="남들은 모르는 나의 취향은?", required = false, dataType = "String")
    private String qnaQuestionMadeFromUser;

    @ApiModelProperty(value = "일상 게시글 제목", example ="날씨가 좋았던 날.", required = false, dataType = "String")
    private String dailyTitle;

    @ApiModelProperty(value = "일상 게시글 이미지", example ="https://ben-today-s-house.s3.ap-northeast-2.amazonaws.com/1868e1ae-3986-43e3-8571-7bc0d176f112-lov.png", required = false, dataType = "String")
    private String dailyImage;

    @ApiModelProperty(value = "게시글 작성 시간", example ="2022-10-14 12:39:40", required = true, dataType = "String")
    private String createdAt;

}
