package com.example.demo.src.autobiography.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class PostAutobiographyReq {
    @NotBlank(message = "자서전 제목이 입력되지 않았습니다.")
    @ApiModelProperty(value = "자서전 제목", example ="조이의 자서전", required = true, dataType = "String")
    private String title;

    @NotBlank(message = "추가설명이 입력되지 않았습니다.")
    @ApiModelProperty(value = "추가설명", example ="2022.03.20 2022.06.23", required = true, dataType = "String")
    private String detail;

    @NotBlank(message = "표지 색상이 입력되지 않았습니다.")
    @ApiModelProperty(value = "표지 색상", example ="#27F10038", required = true, dataType = "String")
    private String coverColor;

    @NotBlank(message = "제목 색상이 입력되지 않았습니다.")
    @ApiModelProperty(value = "제목 색상", example ="#27F10038", required = true, dataType = "String")
    private String titleColor;

    @ApiModelProperty(value = "오브제 색상", example ="2", required = true, dataType = "int")
    private int objetColor;


    @ApiModelProperty(value = "게시글 id 리스트", example ="[1,5,19,4,6]", required = true, dataType = "List")
    private List<Integer> postIds;
}
