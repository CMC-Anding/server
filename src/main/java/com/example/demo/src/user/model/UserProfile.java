package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @ApiModelProperty(value ="프로필 이미지", dataType ="String", required = false, example = "https://ben-today-s-house.s3.ap-northeast-2.amazonaws.com/85915361-5544-4b10-a934-7db7c4d9feb6-Cat03.jpg")
    private String profileImage;

    @ApiModelProperty(value ="닉네임", dataType ="String", required = false, example = "보릿고개너마저")
    private String nickname;

    @ApiModelProperty(value ="소개", dataType ="String", required = false, example = "나는 에픽의 타블로, 삶으로부터 맘으로\n내 맘으로부터 라임으로")
    private String introduction;
}
