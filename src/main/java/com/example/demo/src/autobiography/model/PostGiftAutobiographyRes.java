package com.example.demo.src.autobiography.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel

public class PostGiftAutobiographyRes {
    @ApiModelProperty(value = "선물코드", example ="8gift20221031234508", required = true, dataType = "String")
    private String giftCode;
    
}
