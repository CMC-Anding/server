package com.example.demo.src.autobiography;


import com.example.demo.config.BaseException;
import com.example.demo.src.autobiography.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class AutobiographyProvider {

    private final AutobiographyDao autobiographyDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AutobiographyProvider(AutobiographyDao autobiographyDao, JwtService jwtService) {
        this.autobiographyDao = autobiographyDao;
        this.jwtService = jwtService;
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

    /* 내 자서전 목록 조회*/
    public List<Autobiography> getMyAutobiographyList(int userId) throws BaseException {
        try {
            return autobiographyDao.getMyAutobiographyList(userId);
        } catch (Exception exception) {
            logger.error("getMyAutobiographyList 에러");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 자서전 보기 */
    public PostDetail getMyAutographyPage(int autobiographyId, int page) throws BaseException {
        try {
            PostDetail postDetail = autobiographyDao.getMyAutobiographyPage(autobiographyId, page);
            postDetail.setCurrentPage(page);
            return postDetail;
        } catch (Exception exception) {
            logger.error("getMyAutographyPage 에러");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 내가 선물한 자서전 개수 조회 */
    public int getNumberOfAutographiesGiftedFromMe(int userId) throws BaseException {
        try {
            return autobiographyDao.getNumberOfAutographiesGiftedFromMe(userId);
        } catch (Exception exception) {
            logger.error("getNumberOfAutographiesGiftedFromMe 에러");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 선물받은 자서전 개수 조회 */
    public int getNumberOfAutographiesGiftedFromOthers(int userId) throws BaseException {
        try {
            return autobiographyDao.getNumberOfAutographiesGiftedFromOthers(userId);
        } catch (Exception exception) {
            logger.error("getNumberOfAutographiesGiftedFromOthers 에러");
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 자서전 제작할 게시글 목록 조회 */
    public GetPostsForAutographyRes getPostsForAutography(int userId, String filterId, String sort, String lastCreatedAt) throws BaseException {
        try {
            List<Post> postList = autobiographyDao.getPostsForAutography(userId, filterId, sort, lastCreatedAt);

            GetPostsForAutographyRes getPostsForAutographyRes = new GetPostsForAutographyRes();
            getPostsForAutographyRes.setHasMorePost(false);
            getPostsForAutographyRes.setPostList(postList);
            if (postList.size() > 0) {
                getPostsForAutographyRes.setLastCreatedAt(postList.get(postList.size() - 1).getCreatedAt());
            }



            // 페이지네이션 용도
            // 다음페이지 존재 여부에 따른 반환 객체 데이터 작성
//            if (postList.size() < 19) {
//                getPostsForAutographyRes.setHasMorePost(false);
//                getPostsForAutographyRes.setPostList(postList);
//                if (postList.size() > 0) {
//                    getPostsForAutographyRes.setLastCreatedAt(postList.get(postList.size() - 1).getCreatedAt());
//                }
//
//            } else {
//                getPostsForAutographyRes.setHasMorePost(true);
//                getPostsForAutographyRes.setPostList(postList.subList(0,postList.size()-1));
//                getPostsForAutographyRes.setLastCreatedAt(postList.get(postList.size()-2).getCreatedAt());
//            }

            return getPostsForAutographyRes;

        } catch (Exception exception) {
            logger.error("getPostsForAutography 에러");
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
