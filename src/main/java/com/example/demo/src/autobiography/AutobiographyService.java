package com.example.demo.src.autobiography;

import com.example.demo.config.BaseException;
import com.example.demo.src.autobiography.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;

import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import static com.example.demo.config.BaseResponseStatus.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;

// Service Create, Update, Delete 의 로직 처리
@Service
@Transactional(rollbackFor =  Exception.class)
public class AutobiographyService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AutobiographyDao autobiographyDao;
    private final AutobiographyProvider autobiographyProvider;
    private final JwtService jwtService;

    @Autowired
    public AutobiographyService(AutobiographyDao autobiographyDao, AutobiographyProvider autobiographyProvider, JwtService jwtService) {
        this.autobiographyDao = autobiographyDao;
        this.autobiographyProvider = autobiographyProvider;
        this.jwtService = jwtService;
    }

    // 자서전의 게시글구성 제외한 요소 수정
    public void modifyAutobiographyEtc(int autobiographyId, ModifyAutobiographyEtcReq modifyAutobiographyEtcReq) throws BaseException {
        try{
            int result = autobiographyDao.modifyAutobiographyEtc(autobiographyId, modifyAutobiographyEtcReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_AUTOGRAPHY_ETC);
            }
        }catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 자서전 게시글 구성 수정
    public void modifyAutobiographyPost(int autobiographyId, ModifyAutobiographyPostReq modifyAutobiographyPostReq) throws BaseException {
        try{
            //기존 구성정보 삭제
            int deleteResult = autobiographyDao.deleteAutobiographyPost(autobiographyId);
            if(deleteResult == 0){
                throw new BaseException(DELETE_FAIL_AUTOGRAPHY_POST);
            }

            //새로운 구성정보 추가
            try{
                autobiographyDao.insertAutobiographyPost(autobiographyId, modifyAutobiographyPostReq);
            } catch(Exception exception) {
                exception.printStackTrace();
                throw new BaseException(INSERT_FAIL_AUTOGRAPHY_POST);
            }

        }catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // public int giftAutobiography(int autobiographyId) throws BaseException {
        
    //     LocalDateTime nowDateTime = LocalDateTime.now();
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    //     String dateTimeValueOfGiftCode = nowDateTime.format(formatter);

    //     System.out.println(dateTimeValueOfGiftCode);

    //     String giftCodeBeforeCipher = autobiographyId + "gift" + dateTimeValueOfGiftCode ;

    //     try{   
    //         String giftCodeAfterCipher = new SHA256().encrypt(giftCodeBeforeCipher);


    //     } catch (Exception exception) {

    //     }

    //     try{
    //         int lastInsertId = autobiographyDao.giftAutobiography(autobiographyId);
    //         if(lastInsertId == 0){
    //             throw new BaseException(INSERT_GIFT_CODE_FAIL);
    //         }
    //     }catch(Exception exception) {
    //         throw new BaseException(DATABASE_ERROR);
    //     }
    // }

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
   
}