package com.example.demo.src.user;

import com.example.demo.src.post.model.PostDailyPostReq;
import com.example.demo.src.post.model.PostQnaPostReq;
import com.example.demo.src.post.model.Posts;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.secret.Secret.*;

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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

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
            @ApiResponse(code = 3015, message ="이미 등록된 아이디입니다."),
            @ApiResponse(code = 3016, message ="이미 등록된 닉네임입니다."),
            @ApiResponse(code = 3018 , message = "이미 가입된 전화번호입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다."),
            @ApiResponse(code = 4011 , message = "비밀번호 암호화에 실패하였습니다.")}
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
            @ApiResponse(code = 3014 , message = "없는 아이디거나 비밀번호가 틀렸습니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
    )
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@Valid @RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.login(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 아이디 찾기 API
     * [GET] /app/users/id
     * @return BaseResponse<PostLoginRes>
     */
//    @ApiOperation(value="아이디 찾기 API", notes="핸드폰 번호 인증을 통해 사용자 id를 반환합니다")
//    @ApiResponses({
//            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
//            @ApiResponse(code = 2000 , message = "입력값 오류."),
//            @ApiResponse(code = 3017 , message = "등록되지 않은 전화번호 입니다."),
//            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
//            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
//    )
//    @ResponseBody
//    @GetMapping("/id")
//    public BaseResponse<GetAuthenticationRes> searchUserId(@Valid @RequestBody GetAuthenticationReq getAuthenticationReq){
//
//        try{
//            String phoneNumber = getAuthenticationReq.getPhone();
//            // 등록된 핸드폰 번호인지 체크
//            userProvider.checkPhoneNumber(phoneNumber);
//
//            // 인증번호 생성
//            GetAuthenticationRes getAuthenticationRes = userProvider.createAuthenticationNumber();
//
//            // 인증번호 SMS 보내기
//            Message message = new Message();
//            message.setFrom(SMS_SENDER_PHONE_NUMBER);
//            message.setTo(phoneNumber);
//            message.setText("ANDING 인증번호는 "+getAuthenticationRes.getAuthenticationNumber()+"입니다.");
//            SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
//
//            return new BaseResponse<>(getAuthenticationRes);
//        } catch (BaseException exception){
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }


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

    /**
     * 프로필 수정 API(PATCH)
     * [PATCH] /app/users/profile
     * @return BaseResponse
     */
    @ApiOperation(value="프로필 수정 API", notes="사용자의 프로필(프로필이미지, 닉네임, 소개)를 수정합니다. 값이 변경된 부분만 request body에 넣어서 보내주면 됩니다.")
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다."),
            @ApiResponse(code = 4002 , message = "S3 업로드에 실패했습니다.")
    })
    @ResponseBody
    @PatchMapping(value = "/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse modifyUserProfile(@RequestPart(value="patchUserProfileReq", required=false) PatchUserProfileReq patchUserProfileReq, @ApiParam(value = "image", type = "MultipartFile", required = false, example = "이미지 파일") @RequestPart(value="image", required=false) MultipartFile image) throws IOException {
        try{
            //jwt에서 idx 추출.
            int userId = jwtService.getUserIdx();

            //프로필 수정
            userService.modifyUserProfile(userId, patchUserProfileReq, image);

            return new BaseResponse(SUCCESS);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필 수정 API(PUT)
     * [PUT] /app/users/profile
     * @return BaseResponse
     */
    @ApiOperation(value="프로필 수정 API", notes="사용자의 프로필(프로필이미지, 닉네임, 소개)를 수정합니다. 값이 변경된 부분만 request body에 넣어서 보내주면 됩니다.")
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다."),
            @ApiResponse(code = 4002 , message = "S3 업로드에 실패했습니다.")
    })
    @ResponseBody
    @PutMapping(value = "/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse modifyUserProfilePut(@RequestPart(value="patchUserProfileReq", required=false) PatchUserProfileReq patchUserProfileReq, @ApiParam(value = "image", type = "MultipartFile", required = false, example = "이미지 파일") @RequestPart(value="image", required=false) MultipartFile image) throws IOException {
        try{
            //jwt에서 idx 추출.
            int userId = jwtService.getUserIdx();

            //프로필 수정
            userService.modifyUserProfile(userId, patchUserProfileReq, image);

            return new BaseResponse(SUCCESS);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필 수정 API(POST)
     * [POST] /app/users/profile
     * @return BaseResponse
     */
    @ApiOperation(value="프로필 수정 API", notes="사용자의 프로필(프로필이미지, 닉네임, 소개)를 수정합니다. 값이 변경된 부분만 request body에 넣어서 보내주면 됩니다.")
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다."),
            @ApiResponse(code = 4002 , message = "S3 업로드에 실패했습니다.")
    })
    @ResponseBody
    @PostMapping(value = "/profile", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse modifyUserProfilePost(@RequestPart(value="patchUserProfileReq", required=false) PatchUserProfileReq patchUserProfileReq, @ApiParam(value = "image", type = "MultipartFile", required = false, example = "이미지 파일") @RequestPart(value="image", required=false) MultipartFile image) throws IOException {
        try{
            //jwt에서 idx 추출.
            int userId = jwtService.getUserIdx();

            //프로필 수정
            userService.modifyUserProfile(userId, patchUserProfileReq, image);

            return new BaseResponse(SUCCESS);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필 조회 API
     * [GET] /app/users/profile
     * @return BaseResponse<>
     */
    @ApiOperation(value="프로필 조회 API", notes="사용자의 프로필 이미지 url, 닉네임, 소개를 반환합니다")
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001 , message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002 , message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
    )
    @ResponseBody
    @GetMapping("/profile")
    public BaseResponse<UserProfile> getUserProfile( ){
        try{
            //jwt에서 id 추출.
            int userId = jwtService.getUserIdx();

            UserProfile userProfile = userProvider.getUserProfile(userId);
            return new BaseResponse<>(userProfile);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 나의 게시글 수 조회 API
     * [GET] /app/users/my-posts/number
     * @return BaseResponse<Integer>
     */
    @ApiOperation(value="나의 기록 개수 조회 API", notes="사용자가 작성한 게시글의 개수를 반환합니다")
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001 , message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002 , message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
    )
    @ResponseBody
    @GetMapping("/my-posts/number")
    public BaseResponse<Integer> getNumberOfMyPosts( ){
        try{
            //jwt에서 id 추출.
            int userId = jwtService.getUserIdx();

            int numberOfMyPosts = userProvider.getNumberOfMyPosts(userId);
            return new BaseResponse<>(numberOfMyPosts);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 나의 자서전 개수 조회 API
     * [GET] /app/users/my-autobiographies/number
     * @return BaseResponse<Integer>
     */
    @ApiOperation(value="나의 자서전 개수 조회 API", notes="사용자가 작성한 자서전의 개수를 반환합니다")
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001 , message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002 , message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
    )
    @ResponseBody
    @GetMapping("/my-autobiographies/number")
    public BaseResponse<Integer> getNumberOfMyAutobiographies( ){
        try{
            //jwt에서 id 추출.
            int userId = jwtService.getUserIdx();

            int numberOfMyAutobiographies = userProvider.getNumberOfMyAutobiographies(userId);
            return new BaseResponse<>(numberOfMyAutobiographies);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 내가 선물 받은 자서전 조회 API
     * [GET] /app/users/gifted-autobiographies
     * @return BaseResponse<>
     */
    @ApiOperation(value="내가 선물 받은 자서전 조회 API", notes="사용자가 선물받은 자서전들과 그 개수를 반환합니다")
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2001 , message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002 , message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
    )
    @ResponseBody
    @GetMapping("/gifted-autobiographies")
    public BaseResponse<GetGiftedAutobiographiesRes> getGiftedAutobiographies(){
        try{
            //jwt에서 id 추출.
            int userId = jwtService.getUserIdx();

            GetGiftedAutobiographiesRes getGiftedAutobiographiesRes = userProvider.getGiftedAutobiographies(userId);
            return new BaseResponse<>(getGiftedAutobiographiesRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 회원탈퇴 API
     * [DELETE] /app/users/withdrawl
     * @return BaseResponse
     */
    @ApiOperation(value="회원탈퇴 API", notes="해당 회원의 계정을 삭제합니다") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2000 , message = "입력값 오류."),
            @ApiResponse(code = 3019 , message = "이미 삭제되었거나 존재하지 않는 계정입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
    )
    @ResponseBody
    @DeleteMapping("/withdrawl")
    public BaseResponse deleteUser() {
        try{
            //jwt에서 id 추출.
            int userId = jwtService.getUserIdx();

            userService.deleteUser(userId);
            return new BaseResponse<>(SUCCESS);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 사용자 차단 API
     * [POST] /app/users/block
     * @return BaseResponse
     */
    @ApiOperation(value="사용자 차단 API", notes="해당 회원의 계정을 차단합니다") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청에 성공하였습니다."),
            @ApiResponse(code = 2000 , message = "입력값 오류."),
            @ApiResponse(code = 3019 , message = "이미 차단되었거나 존재하지 않는 계정입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")}
    )
    @ResponseBody
    @PostMapping("/block/{nickname}")
    public BaseResponse blockUser(@ApiParam(value = "nickname", required = true, example = "독산동물주먹") @PathVariable(value = "nickname", required = true) String nickname) {
        try{
            //jwt에서 id 추출.
            int userId = jwtService.getUserIdx();

            userService.blockUser(userId, nickname);
            return new BaseResponse<>(SUCCESS);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
