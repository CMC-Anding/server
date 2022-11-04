package com.example.demo.src.archive.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.security.Timestamp;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

@ToString
@Getter
@Setter
@AllArgsConstructor

public class GetArchiveDailyRes {
    private int postId;
    private String dailyTitle;
    private String dailyImage;
    // @JsonFormat(pattern = "yyyy-MM-DD HH:mm:ss")
    // private Date createdAt;
}