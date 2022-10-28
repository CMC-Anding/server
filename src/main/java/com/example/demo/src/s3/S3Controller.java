package com.example.demo.src.s3;

import java.io.IOException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/app/image")
public class S3Controller {

    private final S3Uploader s3Uploader;

    @PostMapping("/s3-upload/{post-id}")
    public BaseResponse<String> s3UploadFile(@RequestParam("images") MultipartFile multipartFile, @PathVariable("post-id") int postId, @RequestParam(required = false) int fileSize) throws IOException,BaseException  {
        try {
        String url = s3Uploader.upload(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), fileSize, postId);
        return new BaseResponse<>(url);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    
}
