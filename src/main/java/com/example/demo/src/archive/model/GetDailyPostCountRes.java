package com.example.demo.src.archive.model;
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
public class GetDailyPostCountRes {
    @ApiModelProperty(value = "아카이브의 일상 게시글 개수", example = "37", required = true, dataType = "int")
    private int dailyPostCount;
}
