package com.example.demo.src.post.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class DeletePostReq {
    @NotBlank(message = "삭제할 게시글을 선택해주세요.")
    @ApiModelProperty(value = "삭제할 게시글 id", example = "14", required = true, dataType = "int")
    private int postId;
    
}
