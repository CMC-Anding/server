package com.example.demo.src.question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.question.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

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

@RestController
@RequestMapping("/app/questions")
@Api(value = "/app/questions", description = "resource가 Question인 API입니다") // swagger annotation
public class QuestionController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final QuestionProvider questionProvider;
    @Autowired
    private final QuestionService questionService;
    @Autowired
    private final JwtService jwtService;




    public QuestionController(QuestionProvider questionProvider, QuestionService questionService, JwtService jwtService){
        this.questionProvider = questionProvider;
        this.questionService = questionService;
        this.jwtService = jwtService;
    }

     /**
     * 각 필터의 질문 가져오기 API
     * [GET] /app/questions/:filter-id
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ApiOperation(value="각 필터의 질문 가져오기 API", notes="홈(문답 게시글 작성)에서 특정 필터 선택시, 나올 수 있는 질문입니다.") // swagger annotation
    @ResponseBody
    @ApiResponses({
        @ApiResponse(code =4050 , message = "사용자가 작성하지않은 질문을 가져오는데에 실패하였습니다.")}
    )
    @GetMapping("/{filter-id}") // (GET) 127.0.0.1:9000/app/questions/:filter-id
    public BaseResponse<GetQuestionRes> getQuestion(@PathVariable("filter-id") String filterId) {
        try{

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // Get Question
            GetQuestionRes getQuestionRes = questionProvider.getQuestion(filterId, userIdxByJwt);
            return new BaseResponse<>(getQuestionRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    // /**
    //  * 회원 1명 조회 API
    //  * [GET] /users/:userIdx
    //  * @return BaseResponse<GetUserRes>
    //  */
    // // Path-variable
    // @ResponseBody
    // @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    // public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
    //     // Get Users
    //     try{
    //         GetUserRes getUserRes = userProvider.getUser(userIdx);
    //         return new BaseResponse<>(getUserRes);
    //     } catch(BaseException exception){
    //         return new BaseResponse<>((exception.getStatus()));
    //     }

    // }

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
