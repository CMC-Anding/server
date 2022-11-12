package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel
public class PostAutoLoginRes {

    @ApiModelProperty(value = "jwtAccessToken", example ="eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VySWR4Ijo1LCJpYXQiOjE2Njc5ODQzNTUsImV4cCI6MTY5OTUyMDM1NX0.ZjPnrZV1fzKei7B-0NuZVJPT_RgMPneqS90120-b-4I", required = true, dataType = "String")
    private String accessToken;

    @ApiModelProperty(value = "jwtRefreshToken", example ="eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWR4Ijo1LCJpYXQiOjE2Njc5ODQzNTUsImV4cCI6MTY5OTUyMDM1NX0.RaqAPTJ4G2sZYYnP5r_X4hfFvvnfnTQxvkZQsWICtvc", required = true, dataType = "String")
    private String refreshToken;
}
