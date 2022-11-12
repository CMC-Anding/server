package com.example.demo.src.post.model;
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
public class Posts {
    @ApiModelProperty(value = "글 작성자 id", example = "37", required = false, dataType = "int")
    private int userId;

    @ApiModelProperty(value = "게시글 id", example = "37", required = false, dataType = "int")
    private int postId;

    @ApiModelProperty(value = "게시글 내용", example = "어텐션~~", required = false, dataType = "String")
    private String contents;

    @ApiModelProperty(value = "일상 게시글의 제목", example = "뉴진스입니다.", required = false, dataType = "String")
    private String dailyTitle;

    @ApiModelProperty(value = "문답 게시글의 배경색", example = "#DDEB5C", required = false, dataType = "String")
    private String qnaBackgroundColor;

    @ApiModelProperty(value = "문답 게시글의 필터 id", example = "i", required = false, dataType = "String")
    private String filterId;

    @ApiModelProperty(value = "문답 게시글의 질문 id", example = "i-6", required = false, dataType = "String")
    private String qnaQuestionId;

    @ApiModelProperty(value = "문답 게시글의 사용자가 직접 작성한 질문", example = "나는 누구일까?", required = false, dataType = "String")
    private String qnaQuestionMadeFromUser;

    @ApiModelProperty(value = "피드 공유 여부", example = "Y", required = false, dataType = "String")
    private String feedShare;
}