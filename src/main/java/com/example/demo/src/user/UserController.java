package com.example.demo.src.user;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.secret.Secret.*;
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
@RequestMapping("/app/users")
@Api(value = "/app/users", description = "resource가 users인 API입니다") // swagger annotation
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    private final DefaultMessageService messageService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
        this.messageService = NurigoApp.INSTANCE.initialize(COOL_SMS_API_KEY, COOL_SMS_API_SECRET_KEY, "https://api.coolsms.co.kr");
    }

    /**
     * 핸드폰 인증 번호 요청 API
     * [POST] app/users/authentication
     * @return BaseResponse<GetAuthenticationRes>
     */
    @ApiOperation(value="핸드폰 인증번호 요청 API", notes="핸드폰 번호를 입력하면 인증번호를 SMS로 전송합니다.") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청성공"),
            @ApiResponse(code = 2000 , message = "입력값 오류"),
            @ApiResponse(code = 4001 , message = "서버와 연결 실패")}
    )
    @ResponseBody
    @PostMapping("/authentication")
    public BaseResponse<GetAuthenticationRes> getAuthenticationNumber(@Valid @RequestBody GetAuthenticationReq getAuthenticationReq) {
            // 인증번호 생성
            GetAuthenticationRes getAuthenticationRes = userProvider.createAuthenticationNumber();

            // 인증번호 SMS 보내기
            Message message = new Message();
            message.setFrom(SMS_SENDER_PHONE_NUMBER);
            message.setTo(getAuthenticationReq.getPhone());
            message.setText("ANDING 인증번호는 "+getAuthenticationRes.getAuthenticationNumber()+"입니다.");
            SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

            return new BaseResponse<>(getAuthenticationRes);
    }

    /**
     * 아이디 중복 확인 API
     * [POST] app/users/check/id
     * @return BaseResponse
     */
    @ApiOperation(value="아이디 중복 확인 API", notes="등록하려는 아이디의 중복 여부를 판단합니다") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1001 , message = "사용 가능한 아이디입니다."),
            @ApiResponse(code = 3015 , message = "이미 등록된 아이디입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
    )
    @ResponseBody
    @PostMapping("/check/id")
    public BaseResponse checkUserIdDuplication(@Valid @RequestBody PostUserIdCheckReq postUserIdCheckReq) {

        try{
            //아이디 중복 검증
            userProvider.checkUserId(postUserIdCheckReq.getUserId());
            return new BaseResponse<>(VALID_USER_ID);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 닉네임 중복 확인 API
     * [POST] app/users/check/nickname
     * @return BaseResponse
     */
    @ApiOperation(value="닉네임 중복 확인 API", notes="등록하려는 닉네임의 중복 여부를 판단합니다.") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1002 , message = "사용 가능한 닉네임입니다."),
            @ApiResponse(code = 3016 , message = "이미 등록된 닉네임입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
    )
    @ResponseBody
    @PostMapping("/check/nickname")
    public BaseResponse checkNicknameDuplication(@Valid @RequestBody PostNicknameCheckReq postNicknameCheckReq) {

        try{
            //닉네임 중복 검증
            userProvider.checkNickname(postNicknameCheckReq.getNickname());
            return new BaseResponse<>(VALID_NICKNAME);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입 API
     * [POST] /app/users/join
     * @return BaseResponse
     */
    @ApiOperation(value="회원가입 API", notes="id, 비밀번호, 닉네임, 전화번호를 등록합니다.") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2000 , message = "입력값 오류."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
    )
    @ResponseBody
    @PostMapping("/join")
    public BaseResponse createUser(@Valid @RequestBody PostUserReq postUserReq) {
        try{
            userService.createUser(postUserReq);
            return new BaseResponse<>(SUCCESS);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 기본 로그인 API
     * [POST] /app/users/login
     * @return BaseResponse<PostLoginRes>
     */
    @ApiOperation(value="기본 로그인 API", notes="id, 비밀번호로 로그인을 요청하면 jwt 토큰을 반환합니다") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2000 , message = "입력값 오류."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
    )
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.login(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
//    @ResponseBody
//    @PatchMapping("/{userIdx}")
//    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user){
//        try {
//            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
//            //같다면 유저네임 변경
//            PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUserName());
//            userService.modifyUserName(patchUserReq);
//
//            String result = "";
//        return new BaseResponse<>(result);
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }


}
