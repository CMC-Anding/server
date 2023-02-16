package com.example.demo.src.user.model;

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
public class PostUserIdCheckReq {

    @NotBlank
    @ApiModelProperty(value = "사용자 id", example ="abcd1234", required = true, dataType = "String")
    private String userId;
}
