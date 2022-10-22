package com.example.demo.src.archive;

import com.example.demo.config.BaseException;
import com.example.demo.src.archive.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.example.demo.config.BaseResponseStatus.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;

// Service Create, Update, Delete 의 로직 처리
@Service
@Transactional(rollbackFor =  Exception.class)
public class ArchiveService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ArchiveDao archiveDao;
    private final ArchiveProvider archiveProvider;
    private final JwtService jwtService;

    @Autowired
    public ArchiveService(ArchiveDao archiveDao, ArchiveProvider archiveProvider, JwtService jwtService) {
        this.archiveDao = archiveDao;
        this.archiveProvider = archiveProvider;
        this.jwtService = jwtService;
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
   
}
