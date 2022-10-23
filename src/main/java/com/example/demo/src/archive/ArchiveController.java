package com.example.demo.src.archive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.archive.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

import org.springframework.http.MediaType;
// swagger add!!!
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import java.io.IOException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
@RestController
@RequestMapping("/app/archives")
@Api(value = "/app/archives", description = "resource가 Archives인 API입니다") // swagger annotation
public class ArchiveController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ArchiveProvider archiveProvider;
    @Autowired
    private final ArchiveService archiveService;
    @Autowired
    private final JwtService jwtService;

    public ArchiveController(ArchiveProvider archiveProvider, ArchiveService archiveService, JwtService jwtService){
        this.archiveProvider = archiveProvider;
        this.archiveService = archiveService;
        this.jwtService = jwtService;
    }

    /**
     * 아카이브 문답 조회 API
     * [GET] /app/archives/qnas?filterId
     * @return BaseResponse<List<GetArchiveQnaRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("qnas") // (GET) 127.0.0.1:6660/app/archives/qnas?filterId
    public BaseResponse<List<GetArchiveQnaRes>> getArchiveQnaList(@RequestParam(required = false) String filterId) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();

            // Get Archive Qna List
            if(filterId == null){
                List<GetArchiveQnaRes> getArchiveQnaRes = archiveProvider.getArchiveQnaList(userIdxByJwt);
                return new BaseResponse<>(getArchiveQnaRes);
            } else {
                // Get Archive Qna List By FilterId
                List<GetArchiveQnaRes> getArchiveQnaRes = archiveProvider.getArchiveQnaListByFilterId(filterId, userIdxByJwt);
                return new BaseResponse<>(getArchiveQnaRes);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 아카이브 일상 조회 API
     * [GET] /app/archives/daily
     * @return BaseResponse<List<GetArchiveDailyRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("daily") // (GET) 127.0.0.1:6660/app/archives/daily
    public BaseResponse<List<GetArchiveDailyRes>> getArchiveDailyList(@RequestParam(required = false) String filterId) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();

            // Get Archive Daily List
            List<GetArchiveDailyRes> getArchiveDailyRes = archiveProvider.getArchiveDailyList(userIdxByJwt);
            return new BaseResponse<>(getArchiveDailyRes);
            
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    



    // /**
    //  * 회원가입 API
    //  * [POST] /users
    //  * @return BaseResponse<PostUserRes>
    //  */
    // // Body
    // @ResponseBody
    // @PostMapping("")
    // public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
    //     // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
    //     if(postUserReq.getEmail() == null){
    //         return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
    //     }
    //     //이메일 정규표현
    //     if(!isRegexEmail(postUserReq.getEmail())){
    //         return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
    //     }
    //     try{
    //         PostUserRes postUserRes = userService.createUser(postUserReq);
    //         return new BaseResponse<>(postUserRes);
    //     } catch(BaseException exception){
    //         return new BaseResponse<>((exception.getStatus()));
    //     }
    // }
    

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    // @ResponseBody
    // @PatchMapping("/{userIdx}")
    // public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user){
    //     try {
    //         //jwt에서 idx 추출.
    //         int userIdxByJwt = jwtService.getUserIdx();
    //         //userIdx와 접근한 유저가 같은지 확인
    //         if(userIdx != userIdxByJwt){
    //             return new BaseResponse<>(INVALID_USER_JWT);
    //         }
    //         //같다면 유저네임 변경
    //         PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUserName());
    //         userService.modifyUserName(patchUserReq);

    //         String result = "";
    //     return new BaseResponse<>(result);
    //     } catch (BaseException exception) {
    //         return new BaseResponse<>((exception.getStatus()));
    //     }
    // }


}
