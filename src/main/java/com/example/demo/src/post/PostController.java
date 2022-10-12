package com.example.demo.src.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.*;
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
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/app/posts")
@Api(value = "/app/posts", description = "resource가 Post인 API입니다") // swagger annotation
public class PostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PostProvider postProvider;
    @Autowired
    private final PostService postService;
    @Autowired
    private final JwtService jwtService;




    public PostController(PostProvider postProvider, PostService postService, JwtService jwtService){
        this.postProvider = postProvider;
        this.postService = postService;
        this.jwtService = jwtService;
    }


    /**
     * 게시글 등록 API
     * [POST] /app/posts
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<String> postPost(@RequestPart PostPostReq postPostReq, @RequestPart(value="file", required=false) MultipartFile file ){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            postService.postPost(postPostReq, file);
            String result = "게시글이 등록되었습니다!";
            return new BaseResponse<>(SUCCESS ,result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
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
