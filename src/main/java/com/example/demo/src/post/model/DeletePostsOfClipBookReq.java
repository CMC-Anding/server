package com.example.demo.src.post.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class DeletePostsOfClipBookReq {
    @NotBlank(message = "스크랩북에서 삭제할 게시글을 선택해주세요.")
    @ApiModelProperty(value = "스크랩북에서 삭제할 게시글 id", example = "[37,14]", required = true, dataType = "int")
    private List<Integer> postId;
}
