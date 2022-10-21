package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GiftedAutobiography {

    @ApiModelProperty(value ="자서전 ID", dataType ="int", required = true, example = "4")
    private int id;

    @ApiModelProperty(value ="자서전 제목", dataType ="String", required = true, example = "2022년 회고록")
    private String title;

    @ApiModelProperty(value ="자서전 제목 색상", dataType ="String", required = true, example = "kCGColorSpaceModelRGB 0.943611 0.607663 0.456563 1")
    private String titleColor;

    @ApiModelProperty(value ="자서전 표지 색상", dataType ="String", required = true, example = "kCGColorSpaceModelRGB 0.943611 0.607663 0.456563 1")
    private String coverColor;

    @ApiModelProperty(value ="자서전 추가 설명", dataType ="String", required = true, example = "뜨거운 여름밤은 가고 남은 건")
    private String detail;

    @ApiModelProperty(value ="자서전 선물 일자", dataType ="String", required = true, example = "2022-10-21 01:21:08")
    private String giftedAt;
}
