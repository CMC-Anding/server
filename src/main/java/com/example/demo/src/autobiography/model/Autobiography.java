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
public class Autobiography {
    @ApiModelProperty(value = "자서전 id", example ="3", required = true, dataType = "int")
    private int id;

    @ApiModelProperty(value = "자서전 제목", example ="조이의 자서전", required = true, dataType = "String")
    private String title;

    @ApiModelProperty(value = "추가설명", example ="2022.03.20 2022.06.23", required = true, dataType = "String")
    private String detail;

    @ApiModelProperty(value = "표지 색상", example ="kCGColorSpaceModelRGB ", required = true, dataType = "String")
    private String coverColor;

    @ApiModelProperty(value = "제목 색상", example ="kCGColorSpaceModelRGB ", required = true, dataType = "String")
    private String titleColor;

    @ApiModelProperty(value = "작성 시간", example ="2022-10-24 21:46:23", required = true, dataType = "String")
    private String createdAt;

}
