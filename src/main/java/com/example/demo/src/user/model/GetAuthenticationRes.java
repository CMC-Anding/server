package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class GetAuthenticationRes {

    @ApiModelProperty(value ="인증 번호", dataType ="String", required = true, example = "123456")
    private String authenticationNumber;
}
