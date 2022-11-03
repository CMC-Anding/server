package com.example.demo.src.autobiography.model;

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
public class GetPostsForAutographyRes {

    @ApiModelProperty(value = "다음 페이지 존재 여부", example = "true", required = true, dataType = "boolean")
    private boolean hasMorePost;

    @ApiModelProperty(value = "마지막 게시글 작성시간", example = "2022-11-02 15:38:23", required = true, dataType = "String")
    private String lastCreatedAt;

    @ApiModelProperty(value = "게시글 목록", required = true, dataType = "list")
    private List<Post> postList;
}
