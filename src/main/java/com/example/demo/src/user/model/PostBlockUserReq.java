package com.example.demo.src.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.PathVariable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class PostBlockUserReq {
    @ApiModelProperty(value = "nickname", required = true, example = "독산동물주먹")
    private String nickname;
}
