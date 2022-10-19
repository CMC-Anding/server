package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.example.demo.config.BaseResponseStatus.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.transaction.annotation.Transactional;

// Service Create, Update, Delete 의 로직 처리
@Service
@Transactional(rollbackFor =  Exception.class)
public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostDao postDao;
    private final PostProvider postProvider;
    private final JwtService jwtService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;


    @Autowired
    public PostService(PostDao postDao, PostProvider postProvider, JwtService jwtService, AmazonS3 amazonS3) {
        this.postDao = postDao;
        this.postProvider = postProvider;
        this.jwtService = jwtService;
        this.amazonS3 = amazonS3;
    }

    // //POST
    // public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
    //     //중복
    //     if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
    //         throw new BaseException(POST_USERS_EXISTS_EMAIL);
    //     }

    //     String pwd;
    //     try{
    //         //암호화
    //         pwd = new SHA256().encrypt(postUserReq.getPassword());
    //         postUserReq.setPassword(pwd);

    //     } catch (Exception ignored) {
    //         throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
    //     }
    //     try{
    //         int userIdx = userDao.createUser(postUserReq);
    //         //jwt 발급.
    //         String jwt = jwtService.createJwt(userIdx);
    //         return new PostUserRes(jwt,userIdx);
    //     } catch (Exception exception) {
    //         throw new BaseException(DATABASE_ERROR);
    //     }
    // }

    public int postDailyPost(PostDailyPostReq postDailyPostReq) throws IOException,BaseException {
        try{
            int lastInsertId = postDao.postDailyPost(postDailyPostReq);

            if(lastInsertId == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        return lastInsertId;
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String fileUpload(InputStream inputStream, String originalFilename, int postId) throws IOException,BaseException {
        String s3FileName = UUID.randomUUID() + "-" + originalFilename;

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(inputStream.available());

        amazonS3.putObject(bucket, s3FileName, inputStream, objMeta);

        String url = amazonS3.getUrl(bucket, s3FileName).toString();

        try {
            postDao.createImage(url, postId);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }    

    public int postQnaPost(PostQnaPostReq postQnaPostReq) throws IOException,BaseException {
        try{
            int lastInsertId = postDao.postQnaPost(postQnaPostReq);

            if(lastInsertId == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        return lastInsertId;
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
