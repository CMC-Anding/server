package com.example.demo.src.s3;

import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.config.BaseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(InputStream inputStream, String originalFilename , int fileSize, int postId) throws IOException,BaseException {
        String s3FileName = UUID.randomUUID() + "-" + originalFilename;

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(inputStream.available());

        amazonS3.putObject(bucket, s3FileName, inputStream, objMeta);

        String url = amazonS3.getUrl(bucket, s3FileName).toString();

        try {
            S3Dao.createImage(url,postId);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }    
}
