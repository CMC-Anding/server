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
public class ReportPostReq {
    @NotBlank(message = "신고할 게시글을 선택해주세요.")
    @ApiModelProperty(value = "신고할 게시글 id", example = "34", required = true, dataType = "int")
    private int postId;

    @NotBlank(message = "신고 이유를 선택해주세요.")
    @ApiModelProperty(value = "게시글 신고 이유 항목id", example = "3", required = true, dataType = "int")
    private int reasonId;
}
