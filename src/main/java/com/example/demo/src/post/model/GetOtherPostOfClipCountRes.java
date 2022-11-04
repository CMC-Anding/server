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
public class GetOtherPostOfClipCountRes {
    @ApiModelProperty(value = "스크랩북의 타인 게시글 개수", example = "37", required = true, dataType = "int")
    private int otherPostOfClipCount;
}
