package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="프로필 수정 객체", description= "수정된 닉네임, 수정된 소개, 기존 프로필 이미지 삭제여부를 포함한 객체")
public class PatchUserProfileReq {

    @ApiModelProperty(value ="수정된 닉네임", dataType ="String", required = false, example = "보릿고개너마저")
    private String nickname;

    @ApiModelProperty(value ="수정된 소개", dataType ="String", required = false, example = "나는 에픽의 타블로, 삶으로부터 맘으로\n내 맘으로부터 라임으로")
    private String introduction;

    @ApiModelProperty(value ="기존 프로필 이미지 삭제여부", dataType ="boolean", required = true, example = "true")
    private boolean imageDeleted;

}
