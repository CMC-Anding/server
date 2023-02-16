package com.example.demo.src.post.model;
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
@ApiModel
public class GetPostWriterIdRes {
    @ApiModelProperty(value = "게시글 작성자 id", example = "8", required = true, dataType = "int")
    private int writerId;
}
