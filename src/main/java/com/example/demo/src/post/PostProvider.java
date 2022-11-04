package com.example.demo.src.post;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class PostProvider {

    private final PostDao postDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PostProvider(PostDao postDao, JwtService jwtService) {
        this.postDao = postDao;
        this.jwtService = jwtService;
    }

    //글 상세보기
    public GetPostDetailRes getPostDetail(int postId) throws BaseException {
        try {
            GetPostDetailRes getPostDetailRes = postDao.getPostDetail(postId);
            return getPostDetailRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //내 게시글 스크랩 조회
    public List<GetMyClipRes> getMyPostClip(int userIdxByJwt, String chronological) throws BaseException {
        try {
            //시간순
            if(chronological.equals("asc")) {
                List<GetMyClipRes> getMyClipRes = postDao.getMyPostClipChronological(userIdxByJwt);
                return getMyClipRes;
            }
            //최신순
            else {
                List<GetMyClipRes> getMyClipRes = postDao.getMyPostClipReverseChronological(userIdxByJwt);
                return getMyClipRes;
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //타인 게시글 스크랩 조회
    public List<GetMyClipRes> getOtherPostClip(int userIdxByJwt, String chronological) throws BaseException {
        try {
            //시간순
            if(chronological.equals("asc")) {
                List<GetMyClipRes> getMyClipRes = postDao.getOtherPostClipChronological(userIdxByJwt);
                return getMyClipRes;
            }
            //최신순
            else {
                List<GetMyClipRes> getMyClipRes = postDao.getOtherPostClipReverseChronological(userIdxByJwt);
                return getMyClipRes;
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // public List<GetUserRes> getUsersByEmail(String email) throws BaseException{
    //     try{
    //         List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
    //         return getUsersRes;
    //     }
    //     catch (Exception exception) {
    //         throw new BaseException(DATABASE_ERROR);
    //     }
    //                 }

    // public int checkEmail(String email) throws BaseException{
    //     try{
    //         return userDao.checkEmail(email);
    //     } catch (Exception exception){
    //         throw new BaseException(DATABASE_ERROR);
    //     }
    // }

    // public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
    //     User user = userDao.getPwd(postLoginReq);
    //     String encryptPwd;
    //     try {
    //         encryptPwd=new SHA256().encrypt(postLoginReq.getPassword());
    //     } catch (Exception ignored) {
    //         throw new BaseException(PASSWORD_DECRYPTION_ERROR);
    //     }

    //     if(user.getPassword().equals(encryptPwd)){
    //         int userIdx = user.getUserIdx();
    //         String jwt = jwtService.createJwt(userIdx);
    //         return new PostLoginRes(userIdx,jwt);
    //     }
    //     else{
    //         throw new BaseException(FAILED_TO_LOGIN);
    //     }

    // }

}
