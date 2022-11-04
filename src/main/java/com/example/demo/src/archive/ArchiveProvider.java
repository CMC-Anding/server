package com.example.demo.src.archive;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.archive.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Executable;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ArchiveProvider {

    private final ArchiveDao archiveDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ArchiveProvider(ArchiveDao archiveDao, JwtService jwtService) {
        this.archiveDao = archiveDao;
        this.jwtService = jwtService;
    }

    // 아카이브 문답 조회 API
    // Get Archive Qna List (전체필터)
    public List<GetArchiveQnaRes> getArchiveQnaList(int userIdxByJwt, String chronological) throws BaseException {
        try {
            // 시간순
            if(chronological.equals("asc")) {
                List<GetArchiveQnaRes> getArchiveQnaRes = archiveDao.getArchiveQnaListChronological(userIdxByJwt);
                return getArchiveQnaRes;
            }
            else {// 최신순
                List<GetArchiveQnaRes> getArchiveQnaRes = archiveDao.getArchiveQnaListReverseChronological(userIdxByJwt);
                return getArchiveQnaRes;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
        }

    // 아카이브 문답 조회 API
    // Get Archive Qna List By FilterId (개별필터)
    public List<GetArchiveQnaRes> getArchiveQnaListByFilterId(String filterId, int userIdxByJwt, String chronological) throws BaseException {
        try {
            System.out.println(chronological);
            // 시간순
            if(chronological.equals("asc")) {
                List<GetArchiveQnaRes> getArchiveQnaRes = archiveDao.getArchiveQnaListByFilterIdChronological(filterId, userIdxByJwt);
                return getArchiveQnaRes;
            }else { //최신순
                List<GetArchiveQnaRes> getArchiveQnaRes = archiveDao.getArchiveQnaListByFilterIdReverseChronological(filterId, userIdxByJwt);
                return getArchiveQnaRes;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
        }
    
    // 아카이브 일상 조회 API
    public List<GetArchiveDailyRes> getArchiveDailyList(int userIdxByJwt, String chronological) throws BaseException {
    try {
        // 시간순
        if(chronological.equals("asc")) {
            List<GetArchiveDailyRes> getArchiveDailyRes = archiveDao.getArchiveDailyListChronological(userIdxByJwt);
            return getArchiveDailyRes;
        }else{ //최신순
            List<GetArchiveDailyRes> getArchiveDailyRes = archiveDao.getArchiveDailyListReverseChronological(userIdxByJwt);
            return getArchiveDailyRes;
        }
    } catch (Exception exception) {
        exception.printStackTrace();
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
