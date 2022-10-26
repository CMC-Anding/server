package com.example.demo.src.autobiography.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ModifyAutobiographyEtcReq {
    @NotBlank(message = "제목이 입력되지 않았습니다.")
    @ApiModelProperty(value = "자서전 제목", example ="나의 첫번째 이야기", required = true, dataType = "String")
    private String title;

    @NotBlank(message = "추가설명이 입력되지 않았습니다.")
    @ApiModelProperty(value = "자서전 추가설명", example ="2021.02.12 ~ 2022.10.12", required = true, dataType = "String")
    private String detail;

    @NotBlank(message = "표지 색상이 선택되지 않았습니다.")
    @ApiModelProperty(value = "자서전 표지 색상 HEX코드", example ="kCGColorSpaceModelRGB 0.943611 0.607663 0.456563 1", required = true, dataType = "String")
    private String coverColor;

    @NotBlank(message = "제목 색상이 선택되지 않았습니다.")
    @ApiModelProperty(value = "자서전 제목 색상 HEX코드", example ="kCGColorSpaceModelRGB 0.943611 0.607663 0.456563 1", required = true, dataType = "String")
    private String titleColor;   
}
