package com.example.demo.src.autobiography;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.autobiography.model.*;
import com.example.demo.utils.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
import io.swagger.models.auth.In;
import io.swagger.annotations.ApiResponse;

import java.io.IOException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

//@RequiredArgsConstructor
@RestController
@RequestMapping("/app/autobiographies")
@Api(value = "/app/autobiographies", description = "resource가 Autobiography인 API입니다") // swagger annotation
public class AutobiographyController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AutobiographyProvider autobiographyProvider;
    @Autowired
    private final AutobiographyService autobiographyService;
    @Autowired
    private final JwtService jwtService;

    public AutobiographyController(AutobiographyProvider autobiographyProvider, AutobiographyService autobiographyService, JwtService jwtService){
        this.autobiographyProvider = autobiographyProvider;
        this.autobiographyService = autobiographyService;
        this.jwtService = jwtService;
    }

    /*
     * 자서전의 게시글구성 제외한 요소 수정 API
     * [PATCH] /app/autobiographies/etc/:autobiography-id
     * @return BaseRespone<String>
     */
    // Body
    @ApiOperation(value="자서전의 게시글구성 제외한 요소 수정 API", notes="자서전의 제목, 추가설명, 제목색상, 표지색상을 수정합니다.") // swagger annotation
    @ApiResponses({
        @ApiResponse(code = 1000 , message = "요청성공"),
        @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),
        @ApiResponse(code = 4502 , message = "자서전의 제목, 세부설명, 색상의 정보 수정에 실패하였습니다.")}
    )
    @ResponseBody
    @PatchMapping("/etc/{autobiography-id}")
    public BaseResponse<String> modifyAutobiographyEtc(@PathVariable("autobiography-id") int autobiographyId, @RequestBody ModifyAutobiographyEtcReq modifyAutobiographyEtcReq) {
        try {
            autobiographyService.modifyAutobiographyEtc(autobiographyId, modifyAutobiographyEtcReq);
            return new BaseResponse<>("자서전 정보수정에 성공했습니다!");

        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /*
     * 자서전의 게시글 구성 수정 API
     * [PATCH] /app/autobiographies/posts/:autobiography-id
     * @return BaseRespone<String>
     */
    // Body
    @ApiOperation(value="자서전의 게시글 구성 수정 API", notes="자서전의 게시글 구성과 게시글 순서를 수정합니다.") // swagger annotation
    @ApiResponses({
        @ApiResponse(code = 1000 , message = "요청성공"),
        @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),
        @ApiResponse(code = 4503 , message = "기존 자서전의 게시글 구성 삭제에 실패하였습니다."),
        @ApiResponse(code = 4504 , message = "자서전의 게시글 구성 생성에 실패하였습니다.")}
    )
    @ResponseBody
    @PatchMapping("/posts/{autobiography-id}")
    public BaseResponse<String> modifyAutobiographyPost(@PathVariable("autobiography-id") int autobiographyId, @RequestBody ModifyAutobiographyPostReq modifyAutobiographyPostReq) {
        try {            
            autobiographyService.modifyAutobiographyPost(autobiographyId, modifyAutobiographyPostReq);
            
            return new BaseResponse<>("자서전을 구성하는 게시글정보 수정에 성공했습니다!");

        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }





    // /*
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

    /**
     * 자서전 등록 API
     * [POST] /app/autobiographies
     * @return BaseRespone
     */
    // Body
    @ApiOperation(value="자서전 등록 API", notes="자서전을 등록합니다.") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청성공"),
            @ApiResponse(code = 2001 , message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002 , message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")
    })
    @ResponseBody
    @PostMapping("")
    public BaseResponse createAutobiography(@RequestBody @Valid PostAutobiographyReq postAutobiographyReq) {
        try {
            int userId = jwtService.getUserIdx();
            autobiographyService.createAutobiography(userId, postAutobiographyReq);
            return new BaseResponse<>(SUCCESS);

        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 자서전 목록 조회 API
     * [GET] /app/autobiographies
     * @return BaseRespone
     */
    // Body
    @ApiOperation(value="자서전 목록 조회 API", notes="내가 작성한 자서전 리스트를 조회합니다.") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청성공"),
            @ApiResponse(code = 2001 , message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002 , message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<Autobiography>> getMyAutobiographyList() {
        try {
            int userId = jwtService.getUserIdx();
            List<Autobiography> myAutobiographyList =  autobiographyProvider.getMyAutobiographyList(userId);
            return new BaseResponse<>(myAutobiographyList);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 자서전 보기 API
     * [GET] /app/autobiographies/:autobiography-id/pages/:page
     * @return BaseRespone<PostDetail>
     */
    @ApiOperation(value="자서전 보기 API", notes="선택한 자서전에 포함된 특정 페이지를 반환합니다.") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청성공"),
            @ApiResponse(code = 2001 , message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002 , message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("/{autobiography-id}/pages/{page}")
    public BaseResponse<PostDetail> getMyAutographyPage(@PathVariable(value = "autobiography-id", required = true) int autobiographyId, @PathVariable(value = "page", required = true) int page) {
        try {
            int userId = jwtService.getUserIdx();
            PostDetail requestedPage = autobiographyProvider.getMyAutographyPage(autobiographyId, page);

            return new BaseResponse(requestedPage);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 내가 선물한 자서전 개수 API
     * [GET] /app/autobiographies/gifted-from-me/number
     * @return BaseRespone<Integer>
     */
    @ApiOperation(value="내가 선물한 자서전 개수 API", notes="내가 선물한 자서전의 개수를 반환합니다.") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청성공"),
            @ApiResponse(code = 2001 , message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002 , message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("/gifted-from-me/number")
    public BaseResponse<Integer> getNumberOfAutographiesGiftedFromMe() {
        try {
            int userId = jwtService.getUserIdx();
            int numberOfAutographiesGiftedFromMe = autobiographyProvider.getNumberOfAutographiesGiftedFromMe(userId);
            return new BaseResponse(numberOfAutographiesGiftedFromMe);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 선물받은 자서전 개수 API
     * [GET] /app/autobiographies/gifted-from-others/number
     * @return BaseRespone<Integer>
     */
    @ApiOperation(value="선물받은 자서전 개수 API", notes="선물받은 자서전의 개수를 반환합니다.") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청성공"),
            @ApiResponse(code = 2001 , message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002 , message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("/gifted-from-others/number")
    public BaseResponse<Integer> getNumberOfAutographiesGiftedFromOthers() {
        try {
            int userId = jwtService.getUserIdx();
            int numberOfAutographiesGiftedFromMe = autobiographyProvider.getNumberOfAutographiesGiftedFromOthers(userId);
            return new BaseResponse(numberOfAutographiesGiftedFromMe);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 자서전 제작할 내용 조회 API
     * [GET] /app/autobiographies/posts?filter-id={filter-id}&sort={sort}&last-created-at={last-created-at}
     * @return BaseRespone<GetPostsForAutographyRes>
     */
    @ApiOperation(value="자서전 제작할 내용 조회 API", notes="자서전 제작에 사용될 게시글 목록을 조회합니다") // swagger annotation
    @ApiResponses({
            @ApiResponse(code = 1000 , message = "요청성공"),
            @ApiResponse(code = 2001 , message = "JWT를 입력해주세요."),
            @ApiResponse(code = 2002 , message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 4000 , message = "데이터베이스 연결에 실패하였습니다."),
            @ApiResponse(code = 4001 , message = "서버와의 연결에 실패하였습니다.")
    })
    @ResponseBody
    @GetMapping("/posts")
    public BaseResponse<GetPostsForAutographyRes> getPostsForAutography(
            @ApiParam(value = "filter-id", type = "String", required = false, example = "d")
            @RequestParam(value="filter-id", required = false) String filterId,
            @ApiParam(value = "sort", type = "String", required = true, example = "newest")
            @RequestParam(value="sort", required = true) String sort,
            @ApiParam(value = "last-created-at", type = "String", required = false, example = "2022-11-01 02:39:11")
            @RequestParam(value="last-created-at", required = false) String lastCreatedAt) {
        try {
            int userId = jwtService.getUserIdx();
            GetPostsForAutographyRes getPostsForAutographyRes = autobiographyProvider.getPostsForAutography(userId, filterId, sort, lastCreatedAt);
            return new BaseResponse(getPostsForAutographyRes);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
