package com.example.demo.src.autobiography.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ModifyAutobiographyPostReq {
    @NotBlank(message = "자서전을 구성할 게시글을 선택해주세요.")
    @ApiModelProperty(value = "자서전을 구성하는 게시글 ID (배열)", example ="20", required = true, dataType = "int")
    private List<Integer> postId;

    // public int[] getPostIdList() {
    //     return postIdList;
    // }

    // public void setPostId(int id) {
    //     this.postId = id;
    // }

    // public List<ModifyAutobiographyPostReq> getModifyAutobiographyPostReqList() {
    //     return modifyAutobiographyPostReqList;
    // }

    // public void setModifyAutobiographyPostReqList(List<ModifyAutobiographyPostReq> modifyAutobiographyPostReqList) {
	// 	this.modifyAutobiographyPostReqList = modifyAutobiographyPostReqList;
	// }
} 
