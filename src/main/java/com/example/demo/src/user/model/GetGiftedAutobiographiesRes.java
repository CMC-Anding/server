package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetGiftedAutobiographiesRes {

    @ApiModelProperty(value ="자서전 개수", dataType ="int", required = true, example = "10")
    private int numberOfAutobiographies;

    @ApiModelProperty(value ="선물받은 자서전 목록", dataType ="Json list", required = true, example = "4")
    private List<GiftedAutobiography> giftedAutobiographiesList;

}
