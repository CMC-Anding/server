package com.example.demo.src.post;

import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.utils.S3Service;
import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
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

    /* 
     * 게시글 등록 API 
     * 일상 게시글 (사진 제외)
    */
    public int postDailyPost(PostDailyPostReq postDailyPostReq) throws IOException,BaseException {
        try{
            int lastInsertId = postDao.postDailyPost(postDailyPostReq);

            if(lastInsertId == 0){
                throw new BaseException(POST_DAILY_POST_ERROR);
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
            exception.printStackTrace();
            throw new BaseException(S3_FILE_POST_REQ_ERROR);
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
                throw new BaseException(POST_QNA_POST_ERROR);
            }
        return lastInsertId;
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 
     * 게시글 수정 API 
     * 일상 게시글 (사진 제외)
    */
    public void updateDailyPost(PostDailyPostReq postDailyPostReq, int postId) throws IOException,BaseException {
        try{
            postDao.updateDailyPost(postDailyPostReq, postId);
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(UPDATE_DAILY_POST_ERROR);
        }
    }

    /* 
     * 게시글 수정 API 
     * s3에서 객체 삭제
    */
    public void s3ObjectDelete(int postId) throws IOException,BaseException {
        try {
            GetImageUrlRes getImageUrlRes = postDao.getImageUrl(postId);
            String imageUrl = getImageUrlRes.getImageUrl();
            s3Service.fileDelete(imageUrl);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(S3_FILE_DELETE_REQ_ERROR);
        }
    }

    /* 
     * 게시글 수정 API 
     * 일상 게시글의 사진 수정
    */
    public String fileUpdate(MultipartFile image, int postId) throws IOException,BaseException {
        String updateImageUrl = "";
        try {
            updateImageUrl = s3Service.fileUpload(image);
            postDao.updateImage(updateImageUrl, postId);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(UPDATE_IMAGE_ERROR);
        }
        return updateImageUrl;
    }  

    /* 
     * 게시글 수정 API 
     * 문답 게시글
    */
    public void updateQnaPost(PostQnaPostReq postQnaPostReq, int postId) throws IOException,BaseException {
        try{
            postDao.updateQnaPost(postQnaPostReq, postId);
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(UPDATE_QNA_POST_ERROR);
        }
    }

    // 게시글 삭제 API 
    public void deletePost(int postId) throws BaseException {
        try{
            postDao.deletePost(postId);
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(POST_DELETE_ERROR);
        }
    }

    // 일상 게시글의 사진 삭제 API
    public void deletePhotoOfDailyPost(int postId) throws BaseException {
        try{
            postDao.deletePhotoOfDailyPost(postId);
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(PHOTO_DELETE_ERROR);
        }
    }

    // 게시글 신고 7회 이상시, 게시글 삭제 API
    public void deletePostWhenReporting(int postId) throws BaseException {
        try{
            postDao.deletePostWhenReporting(postId);
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(POST_DELETE_WHEN_REPORTING_ERROR);
        }
    }

    // 게시글 신고 7회 이상시, 일상 게시글의 사진 삭제 API
    public void deletePhotoOfDailyPostWhenReporting(int postId) throws BaseException {
        try{
            postDao.deletePhotoOfDailyPostWhenReporting(postId);
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(PHOTO_DELETE_WHEN_REPORTING_ERROR);
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
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 스크랩 중복 확인
    public String clipDuplicateCheck(int userIdxByJwt, int postId) throws BaseException {
        try {
            String checkDuplicate = "존재";
            ClipDuplicateCheckRes clipDuplicateCheckRes = postDao.clipDuplicateCheck(userIdxByJwt, postId);
            Integer clipCount = clipDuplicateCheckRes.getClipCount();
            if(clipCount > 0) {
                return checkDuplicate;
            }
            checkDuplicate = "없다";
            return checkDuplicate;
        }catch (Exception exception) {
            throw new BaseException(CLIP_DUPLICATE_CHECK_ERROR);

        }
    }

    // 스크랩 취소 API 
    public void deletePostsOfClipBook(int userIdxByJwt, DeletePostsOfClipBookReq deletePostsOfClipBookReq) throws BaseException {
        try{
            postDao.deletePostsOfClipBook(userIdxByJwt, deletePostsOfClipBookReq);
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DELETE_CLIP_FAIL);
        }
    }

     // 게시글 신고하기 API 
    public int reportPost(int userIdxByJwt,ReportPostReq reportPostReq) throws BaseException {
        try{
            int lastInsertId = postDao.reportPost(userIdxByJwt,reportPostReq);
            if(lastInsertId == 0){
                throw new BaseException(REPORT_POST_ERROR);
            }
        return lastInsertId;
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}