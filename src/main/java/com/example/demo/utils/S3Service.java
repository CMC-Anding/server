package com.example.demo.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
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
import com.amazonaws.AmazonServiceException;

@RequiredArgsConstructor
@Service
public class S3Service {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    
     // application.yml
    @Value("${cloud.aws.s3.bucket}")   
    private String bucket;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

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

    //파일 삭제
    public void fileDelete(String fileUrl) throws BaseException {
        try{
            String fileKey = fileUrl.substring(58);
            String key = fileKey; // 폴더/파일.확장자
            final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();

            try {
                s3.deleteObject(bucket, key);
            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }

            System.out.println(String.format("[%s] deletion complete", key));

        } catch (Exception exception) {
            throw new BaseException(S3_DELETE_ERROR);
        }
    }
}
