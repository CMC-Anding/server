package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostDailyPostReq {
    private int userId;
    private String dailyTitle;
    private String contents;
    private String filterId;
    private String feedShare;
}
