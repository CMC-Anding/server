package com.example.demo.src.post.model;
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
public class GetReportReasonRes {
    @NotBlank(message = "신고항목이 선택되지 않았습니다.")
    @ApiModelProperty(value = "신고 항목 ID", example = "2", required = true, dataType = "int")
    private int reasonId;

    @NotBlank(message = "신고항목이 선택되지 않았습니다.")
    @ApiModelProperty(value = "신고 항목명", example = "스팸홍보/도배글입니다", required = true, dataType = "String")
    private String reasonContents;
    
}
