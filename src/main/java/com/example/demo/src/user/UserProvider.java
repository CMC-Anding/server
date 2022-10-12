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

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.getPwd(postLoginReq);
        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            int userIdx = user.getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt);
        }
        else{
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
