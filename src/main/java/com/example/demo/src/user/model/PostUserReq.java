package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {

    @NotBlank(message = "id가 입력되지 않았습니다.")
    private String userId;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    private String password;

    @NotBlank(message = "닉네임이 입력되지 않았습니다.")
    private String nickname;

    @NotBlank(message = "전화번호가 입력되지 않았습니다.")
    private String phone;
}
