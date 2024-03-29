package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.S3Service;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Transactional(rollbackFor = Exception.class)
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;

    private final S3Service s3Service;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, S3Service s3Service) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.s3Service = s3Service;
    }

    /* 회원가입 */
    public void createUser(PostUserReq postUserReq) throws BaseException {
        //전화번호 중복 체크
        userProvider.checkPhoneNumberDuplicated(postUserReq.getPhone());

        //아이디 중복 체크
        userProvider.checkUserId(postUserReq.getUserId());

        //닉네임 중복 체크
        userProvider.checkNickname(postUserReq.getNickname());

        try {
            //비밀번호 암호화
            String pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try {
            //회원 등록
            int userIdx = userDao.createUser(postUserReq);
        } catch (Exception exception) {
            logger.error("createUser 에러", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 프로필 수정 */
    public void modifyUserProfile(int userId, PatchUserProfileReq patchUserProfileReq, MultipartFile image) throws BaseException {
        try {
            String profileImageUrl=null;
            if (image != null) {
                //새 이미지 추가
                profileImageUrl = s3Service.fileUpload(image);
            }
            userDao.modifyUserProfile(userId, patchUserProfileReq, profileImageUrl);
        } catch (Exception exception) {
            logger.error("modifyUserProfile 에러", exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /* 회원 탈퇴 */
    public void deleteUser(int userId) throws BaseException {
        try {
            if (userDao.deleteUser(userId) == 0) {
                throw new BaseException(FAILED_TO_DELETE_ACCOUNT);
            }
        } catch (BaseException baseException) {
            throw baseException;
        } catch (Exception exception) {
            logger.error("deleteUser 에러", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 사용자 차단 */
    public void blockUser(int userId, PostBlockUserReq postBlockUserReq) throws BaseException {
        try {
            if (userDao.blockUser(userId, postBlockUserReq.getNickname()) != 1) {
                throw new BaseException(FAILED_TO_BLOCK_ACCOUNT);
            }
        } catch (BaseException baseException) {
            throw baseException;
        } catch (Exception exception) {
            logger.error("blockUser 에러", exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }


//    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
//        try{
//            int result = userDao.modifyUserName(patchUserReq);
//            if(result == 0){
//                throw new BaseException(MODIFY_FAIL_USERNAME);
//            }
//        } catch(Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
}
