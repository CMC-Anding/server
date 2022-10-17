package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@ApiModel
public class PostUserReq {

    @NotBlank(message = "id가 입력되지 않았습니다.")
    @ApiModelProperty(value = "사용자 id", example ="abcd1234", required = true, dataType = "String")
    private String userId;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    @ApiModelProperty(value = "비밀번호", example ="abcd1234!@#", required = true, dataType = "String")
    private String password;

    @NotBlank(message = "닉네임이 입력되지 않았습니다.")
    @ApiModelProperty(value = "닉네임", example ="보리꼬리너마저", required = true, dataType = "String")
    private String nickname;

    @NotBlank(message = "전화번호가 입력되지 않았습니다.")
    @ApiModelProperty(value = "전화번호", example ="01056781234", required = true, dataType = "String")
    private String phone;
}
