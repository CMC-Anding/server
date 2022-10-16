package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginReq {

    @NotBlank(message = "id가 입력되지 않았습니다.")
    @ApiModelProperty(value = "사용자 id", example ="abcd1234", required = true, dataType = "String")
    private String userId;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    @ApiModelProperty(value = "비밀번호", example ="abcd1234!@#", required = true, dataType = "String")
    private String password;
}
