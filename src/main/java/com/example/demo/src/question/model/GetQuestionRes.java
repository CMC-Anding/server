package com.example.demo.src.question.model;

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
public class GetQuestionRes {
    @ApiModelProperty(value = "질문 Id", example = "b-6", required = false, dataType = "String")
    private String qnaQuestionId;

    @ApiModelProperty(value = "질문내용", example = "꼭 만나보고 싶은 사람이 있나요?", required = false, dataType = "String")
    private String contents;

    @ApiModelProperty(value = "각 필터의 남은 질문 개수", example = "4", required = false, dataType = "int")
    private int remaningNumberOfFilter;

    @ApiModelProperty(value = "전체의 남은 질문 개수", example = "60", required = false, dataType = "int")
    private int remaningNumberOfAll;
}
