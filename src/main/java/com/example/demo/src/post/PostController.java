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

import java.io.IOException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
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
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}) // (POST) 127.0.0.1:6660/app/posts
    public BaseResponse<String> postPost(@RequestPart Posts posts, @RequestPart(value="file", required=false) MultipartFile file) throws IOException{

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // 일상 게시글 
            if(posts.getDailyTitle() != null) {
                PostDailyPostReq postDailyPostReq = new PostDailyPostReq(userIdxByJwt, posts.getDailyTitle(), posts.getContents(), posts.getFeedShare());
                int lastInsertId = postService.postDailyPost(postDailyPostReq); //선 (사진제외 업로드)
                if(file.isEmpty() == false) {   
                    postService.fileUpload(file.getInputStream(), file.getOriginalFilename(), lastInsertId); //후 (사진 업로드)
                }
            }
            
            // 문답 게시글 
            else {
                PostQnaPostReq postQnaPostReq = new PostQnaPostReq(userIdxByJwt, posts.getFilterId(), posts.getQnaQuestionId(), posts.getContents(), posts.getQnaBackgroundColor(), posts.getQnaQuestionMadeFromUser(), posts.getFeedShare());

                postService.postQnaPost(postQnaPostReq);
            }

            String result = "게시글이 등록되었습니다!";
            return new BaseResponse<>(SUCCESS ,result); 
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 글 상세보기 API
     * [GET] /app/posts/detail/:post-id
     * @return BaseResponse<GetPostDetailRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/detail/{post-id}") // (GET) 127.0.0.1:6660/app/posts/detail/:post-id
    public BaseResponse<GetPostDetailRes> getPostDetail(@PathVariable("post-id") int postId) {
        // Get Users
        try{
            GetPostDetailRes getPostDetailRes = postProvider.getPostDetail(postId);
            return new BaseResponse<>(getPostDetailRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스크랩 API
     * [POST] /app/posts/clip/:post-id
     * @return BaseResponse<String>
     */
    @ApiOperation(value="스크랩 API", notes="내가 작성한 글과 다른 사용자가 작성한 글을 스크랩합니다.") // swagger annotation
    @ApiResponses({
        @ApiResponse(code = 1000 , message = "요청성공"),
        @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),
        @ApiResponse(code = 4505, message = "나의 글 혹은 익명의 글을 스크랩하는데 실패하였습니다.")}
    )
    @ResponseBody
    @PostMapping(value = "/clip/{post-id}") // (POST) 127.0.0.1:6660/app/posts/clip/:post-id
    public BaseResponse<String> postClip(@PathVariable("post-id") int postId) throws BaseException{
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            postService.postClip(userIdxByJwt, postId);

            String result = "스크랩에 성공했습니다!";
            return new BaseResponse<>(SUCCESS ,result); 
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 스크랩북의 게시글 삭제 API
     * [DELETE] /app/posts/clip
     * @return BaseResponse<String>
     */
    @ApiOperation(value="스크랩북의 게시글 삭제 API", notes="스크랩북에서 내가 원하는 게시글을 삭제합니다.(내 게시글, 타인 게시글 모두 가능)") // swagger annotation
    @ApiResponses({
        @ApiResponse(code = 1000 , message = "요청성공"),
        @ApiResponse(code = 4000, message = "데이터베이스 연결에 실패하였습니다."),
        @ApiResponse(code = 4506, message = "스크랩북 수정(구성 게시글 삭제)에 실패하였습니다.")}
    )
    @ResponseBody
    @DeleteMapping(value = "/clip") // (DELETE) 127.0.0.1:6660/app/posts/clip
    public BaseResponse<String> deletePostsOfClipBook(@RequestBody DeletePostsOfClipBookReq deletePostsOfClipBookReq) throws BaseException{
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            postService.deletePostsOfClipBook(userIdxByJwt, deletePostsOfClipBookReq);
            String result = "스크랩북을 수정(구성 게시글 삭제)하였습니다!";
            return new BaseResponse<>(SUCCESS ,result); 
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 내 게시글 스크랩 조회 API
     * [GET] /app/posts/my-clip?chronological
     * @return BaseResponse<GetPostDetailRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/my-clip") // (GET) 127.0.0.1:6660/app/posts/my-clip?chronological
    public BaseResponse<List<GetMyClipRes>> getMyPostClip(@RequestParam(required = true, defaultValue="desc") String chronological) throws BaseException{

        int userIdxByJwt = jwtService.getUserIdx();
        
        try{
            List<GetMyClipRes> getMyClipRes = postProvider.getMyPostClip(userIdxByJwt, chronological);
            return new BaseResponse<>(getMyClipRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상대 게시글 스크랩 조회 API
     * [GET] /app/posts/other-clip?chronological
     * @return BaseResponse<GetPostDetailRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/other-clip") // (GET) 127.0.0.1:6660/app/posts/other-clip?chronological
    public BaseResponse<List<GetMyClipRes>> getOtherPostClip(@RequestParam(required = true, defaultValue="desc") String chronological) throws BaseException{

        int userIdxByJwt = jwtService.getUserIdx();
        
        try{
            List<GetMyClipRes> getMyClipRes = postProvider.getOtherPostClip(userIdxByJwt, chronological);
            return new BaseResponse<>(getMyClipRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스크랩북의 내 게시글 개수 API
     * [GET] /app/posts/clip/my-post/cnt
     * @return BaseResponse<GetMyPostOfClipCountRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/clip/my-post/cnt") // (GET) 127.0.0.1:6660/app/posts/clip/my-post/cnt
    public BaseResponse<GetMyPostOfClipCountRes> getMyPostOfClipCount() {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            GetMyPostOfClipCountRes getMyPostOfClipCountRes = postProvider.getMyPostOfClipCount(userIdxByJwt);

            return new BaseResponse<>(getMyPostOfClipCountRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 스크랩북의 타인 게시글 개수 API
     * [GET] /app/posts/clip/other-post/cnt
     * @return BaseResponse<GetArchiveCntRes>
     */
    //Query String
    @ResponseBody
    @GetMapping("/clip/other-post/cnt") // (GET) 127.0.0.1:6660/app/posts/clip/other-post/cnt
    public BaseResponse<GetOtherPostOfClipCountRes> getOtherPostOfClipCount() {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            GetOtherPostOfClipCountRes getOtherPostOfClipCountRes = postProvider.getOtherPostOfClipCount(userIdxByJwt);

            return new BaseResponse<>(getOtherPostOfClipCountRes);
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
