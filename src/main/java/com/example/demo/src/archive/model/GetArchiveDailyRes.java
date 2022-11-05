package com.example.demo.src.archive.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class GetArchiveDailyRes {
    private int postId;
    private String dailyTitle;
    private String dailyImage;
    // @JsonFormat(pattern = "yyyy-MM-DD HH:mm:ss")
    // private Date createdAt;
}