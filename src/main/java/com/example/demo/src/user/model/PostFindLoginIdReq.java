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
public class PostFindLoginIdReq {

    @NotBlank
    @ApiModelProperty(value = "전화번호", example ="01024681357", required = true, dataType = "String")
    private String phone;

    @NotBlank
    @ApiModelProperty(value = "인증번호", example ="863587", required = true, dataType = "String")
    private String authenticationCode;
}
