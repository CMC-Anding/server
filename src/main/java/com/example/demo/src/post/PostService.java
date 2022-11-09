package com.example.demo.src.post;

import com.example.demo.utils.S3Service;
import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.S3Service;
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
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    public PostService(PostDao postDao, PostProvider postProvider, JwtService jwtService, S3Service s3Service) {
        this.postDao = postDao;
        this.postProvider = postProvider;
        this.jwtService = jwtService;
        this.s3Service = s3Service;
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

    /* 
     * 게시글 등록 API 
     * 일상 게시글
    */
    public int postDailyPost(PostDailyPostReq postDailyPostReq) throws IOException,BaseException {
        try{
            int lastInsertId = postDao.postDailyPost(postDailyPostReq);

            if(lastInsertId == 0){
                throw new BaseException(POST_DAILY_POST_FAIL);
            }
        return lastInsertId;
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 
     * 게시글 등록 API 
     * 일상 게시글의 사진 업로드
    */
    public String fileUpload(MultipartFile image, int postId) throws IOException,BaseException {
        String postImageUrl = "";
        try {
            postImageUrl = s3Service.fileUpload(image);
            postDao.createImage(postImageUrl, postId);
        }catch (Exception exception){
            logger.error("S3 ERROR", exception);
            throw new BaseException(DATABASE_ERROR);
        }
        return postImageUrl;
    }    

    /* 
     * 게시글 등록 API 
     * 문답 게시글
    */
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

    // 스크랩 API 
    public int postClip(int userIdxByJwt, int postId) throws BaseException {
        try{
            int lastInsertId = postDao.postClip(userIdxByJwt, postId);

            if(lastInsertId == 0){
                throw new BaseException(CLIP_FAIL);
            }
        return lastInsertId;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 스크랩 취소 API 
    public void deletePostsOfClipBook(int userIdxByJwt, DeletePostsOfClipBookReq deletePostsOfClipBookReq) throws BaseException {
        try{
            postDao.deletePostsOfClipBook(userIdxByJwt, deletePostsOfClipBookReq);
        } catch(Exception exception){
            throw new BaseException(DELETE_CLIP_FAIL);
        }
    }

    // // 스크랩 하나씩 삭제 API 
    // public void deleteClip(int postId, int userIdxByJwt) throws BaseException {
    //     try{
    //         postDao.deleteClip(postId, userIdxByJwt);

    //     } catch(Exception exception){
    //         throw new BaseException(DELETE_CLIP_FAIL);
    //     }
    // }
}