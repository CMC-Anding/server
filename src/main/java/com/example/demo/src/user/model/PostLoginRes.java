package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginRes {

    @ApiModelProperty(value = "jwt 토큰", example ="eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VySWR4Ijo1LCJpYXQiOjE2NjU3MzIyNDUsImV4cCI6MTY2NzIwMzQ3NH0.XMwds1FNYEbV_bw77_mqzonypeUbukGFwUnFF-4LGAg", required = true, dataType = "String")
    private String jwt;

    @ApiModelProperty(value = "닉네임", example ="보리꼬리너마저", required = true, dataType = "String")
    private String nickname;
}
