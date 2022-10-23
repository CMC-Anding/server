package com.example.demo.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.config.BaseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.*;

@RequiredArgsConstructor
@Service
public class S3Service {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${cloud.aws.s3.bucket}")    // application.yml
    private String bucket;

    private final AmazonS3 amazonS3;


    /** 파일 업로드 (1개) **/
    public String fileUpload(MultipartFile multipartFile) throws BaseException {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getSize());
        objMeta.setContentType(multipartFile.getContentType());

        try {
            // 파일 업로드 후 URL 저장
            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objMeta);
        } catch (Exception e){
            logger.error("S3 ERROR", e);
            // 이미지 업로드 에러
            throw new BaseException(S3_UPLOAD_ERROR);
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

}
