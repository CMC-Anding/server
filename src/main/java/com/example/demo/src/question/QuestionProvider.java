package com.example.demo.src.question;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.question.model.*;
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
public class QuestionProvider {

    private final QuestionDao questionDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public QuestionProvider(QuestionDao questionDao, JwtService jwtService) {
        this.questionDao = questionDao;
        this.jwtService = jwtService;
    }

    public GetQuestionRes getQuestion(String filterId, int userIdxByJwt) throws BaseException{
        try{
            GetQuestionRes getQuestionRes = questionDao.getQuestion(filterId,userIdxByJwt);
            return getQuestionRes;
        }
        catch (Exception exception) {
            throw new BaseException(GET_QUESTION_ERROR);
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


    // public GetUserRes getUser(int userIdx) throws BaseException {
    //     try {
    //         GetUserRes getUserRes = userDao.getUser(userIdx);
    //         return getUserRes;
    //     } catch (Exception exception) {
    //         throw new BaseException(DATABASE_ERROR);
    //     }
    // }

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
