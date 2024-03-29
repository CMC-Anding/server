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
            throw new BaseException(GET_MY_POST_OF_CLIPBOOK_FAIL);
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
            throw new BaseException(GET_OTHER_POST_OF_CLIPBOOK_FAIL);
        }
    }

    // 스크랩북의 내 게시글 개수 
    public GetMyPostOfClipCountRes getMyPostOfClipCount(int userIdxByJwt) throws BaseException {
        try {
            GetMyPostOfClipCountRes getMyPostOfClipCountRes = postDao.getMyPostOfClipCount(userIdxByJwt);
            return getMyPostOfClipCountRes;
        } catch (Exception exception) {
            throw new BaseException(GET_CLIPBOOK_MY_POST_COUNT_FAIL);
        }
    }

    // 스크랩북의 타인 게시글 개수 
    public GetOtherPostOfClipCountRes getOtherPostOfClipCount(int userIdxByJwt) throws BaseException {
        try {
            GetOtherPostOfClipCountRes getOtherPostOfClipCountRes = postDao.getOtherPostOfClipCount(userIdxByJwt);
            return getOtherPostOfClipCountRes;
        } catch (Exception exception) {
            throw new BaseException(GET_CLIPBOOK_OTHER_POST_COUNT_FAIL);
        }
    }

    // 신고 항목 조회
    public List<GetReportReasonRes> getReportReason() throws BaseException {
        try {
            List<GetReportReasonRes> getReportReasonRes = postDao.getReportReason();
            return getReportReasonRes;
        } catch (Exception exception) {
            throw new BaseException(GET_REPORT_REASON_FAIL);
        }
    }

    // 게시글 신고가 7회 이상인지 조회
    public String checkReportCountForDelete(ReportPostReq reportPostReq) throws BaseException{
        try {
            CheckReportCountForDeleteRes checkReportCountForDeleteRes = postDao.getTotalReportCount(reportPostReq);
            String resultForDelete = checkReportCountForDeleteRes.getShouldRemoveOrNot();
            return resultForDelete;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(CHECK_REPORT_COUNT_ERROR);
        }
    }

    // 일상 게시글인지, 문답 게시글인지 확인
    public String checkDailyPostOrQnaPost(int postId) throws BaseException {
        try {
            CheckDailyPostOrQnaPostRes checkDailyPostOrQnaPostRes = postDao.checkDailyPostOrQnaPost(postId);
            String dailyPostOrQna = checkDailyPostOrQnaPostRes.getDailyOrQna();
            return dailyPostOrQna;
        } catch(Exception exception) {
            exception.printStackTrace();
            throw new BaseException(CHECK_DAILY_OR_REPORT_ERROR);
        }
    }

    // 게시글 작성자 ID 조회 
    public int getWriterId(int postId) throws BaseException {
        try {
            GetPostWriterIdRes getPostWriterIdRes = postDao.getWriterId(postId);
            int writerId = getPostWriterIdRes.getWriterId();
            return writerId;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(GET_USER_ID_ERROR);
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
