package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
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
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    /* 기본 로그인*/
    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException{

        // 아이디에 해당하는 User 불러오기
        User user;
        try {
            user = userDao.getUser(postLoginReq);
        } catch (Exception exception) {
            logger.error("login 에러",exception);
            throw new BaseException(FAILED_TO_LOGIN);
        }

        // 비밀번호 암호화
        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            logger.error("login 에러",ignored);
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        // 비밀번호 검증 및 JWT 생성
        if(user.getPassword().equals(encryptPwd)){
            int id = user.getId();
            String jwt = jwtService.createJwt(id);

            return new PostLoginRes(jwt,user.getNickname());
        }
        else{
            logger.error("login 에러");
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    /* 인증번호 생성 */
    public GetAuthenticationRes createAuthenticationNumber() {
        //난수 생성
        double randomValue = Math.random()+1;
        //6자리 정수로 이루어진 문자열 생성
        String authenticationNumber = Integer.toString((int) (randomValue * 1000000)).substring(1);

        return new GetAuthenticationRes(authenticationNumber);
    }

    /* 사용자 ID 중복 검사 */
    public void checkUserId(String userId) throws BaseException {
        try {
            if(userDao.checkUserId(userId)==1){
                throw new BaseException(DUPLICATED_USER_ID);
            }
        } catch (BaseException baseException) {
            logger.error("checkUserId 에러", baseException);
            throw baseException;
        } catch (Exception exception) {
            logger.error("checkUserId 에러", exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /* 닉네임 중복 검사 */
    public void checkNickname(String nickname) throws BaseException {
        try {
            if(userDao.checkNickname(nickname)==1){
                throw new BaseException(DUPLICATED_NICKNAME);
            }
        } catch (BaseException baseException) {
            logger.error("checkNickname 에러", baseException);
            throw baseException;
        } catch (Exception exception) {
            logger.error("checkNickname 에러", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
