package com.example.demo.src.post;


import com.example.demo.src.post.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* 
     * 게시글 등록 API 
     * 일상 게시글
    */
    public int postDailyPost(PostDailyPostReq postDailyPostReq){
        String postDailyPostQuery = "insert into POST (USER_ID, DAILY_TITLE, CONTENTS, FILTER_ID, FEED_SHARE) VALUES (?, ?, ?, ?, ?);";
        Object[] postDailyPostParams = new Object[]{postDailyPostReq.getUserId(), postDailyPostReq.getDailyTitle(), postDailyPostReq.getContents(), "e", postDailyPostReq.getFeedShare()};
        this.jdbcTemplate.update(postDailyPostQuery, postDailyPostParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    /* 
     * 게시글 등록 API 
     * 일상 게시글의 사진 업로드
    */
    public void createImage(String url, int postId){
        String createImageQuery = "insert into POST_PHOTO (URL, POST_ID) values (?, ?)";
        Object[] createImageParams = new Object[]{url, postId};
        this.jdbcTemplate.update(createImageQuery, createImageParams);
    }

    /* 
     * 게시글 등록 API 
     * 문답 게시글
    */
    public int postQnaPost(PostQnaPostReq postQnaPostReq){
        String postQnaPostQuery = "insert into POST (USER_ID, FILTER_ID, QNA_QUESTION_ID, CONTENTS, QNA_BACKGROUND_COLOR, QNA_QUESTION_MADE_FROM_USER, FEED_SHARE) VALUES (?,?,?,?,?,?,?)";
        Object[] postQnaPostParams = new Object[]{postQnaPostReq.getUserId(), postQnaPostReq.getFilterId(), postQnaPostReq.getQnaQuestionId(), postQnaPostReq.getContents(), postQnaPostReq.getQnaBackgroundColor(), postQnaPostReq.getQnaQuestionMadeFromUser(), postQnaPostReq.getFeedShare()};
        return this.jdbcTemplate.update(postQnaPostQuery, postQnaPostParams);
    }

    // 글 상세보기 
    public GetPostDetailRes getPostDetail(int postId){
        String getPostDetailQuery = "select CONTENTS as contents , DAILY_TITLE as dailyTitle, QNA_BACKGROUND_COLOR as qnaBackgroundColor, FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, (select CONTENTS FROM QUESTION WHERE ID = (select QNA_QUESTION_ID FROM POST WHERE ID =?)) as qnaQuestion,(select URL FROM POST_PHOTO WHERE POST_ID = ?) as dailyImage, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST WHERE ID = ?";
        return this.jdbcTemplate.queryForObject(getPostDetailQuery,
                (rs, rowNum) -> new GetPostDetailRes(
                        rs.getString("contents"),
                        rs.getString("dailyTitle"),
                        rs.getString("qnaBackgroundColor"),
                        rs.getString("filterId"),
                        rs.getString("qnaQuestionId"),
                        rs.getString("qnaQuestion"),
                        rs.getString("dailyImage"),
                        rs.getString("qnaQuestionMadeFromUser")),
                    postId, postId, postId);
    }
    
    // 스크랩 
    public int postClip(int userIdxByJwt, int postId){
        String postClipQuery = "insert into CLIP(USER_ID, WRITER_ID, POST_ID) values (?,(select USER_ID from POST where ID = ?),?)";
        Object[] postClipParams = new Object[]{userIdxByJwt, postId, postId};
        return this.jdbcTemplate.update(postClipQuery, postClipParams);
    }

    // 스크랩 취소
    public int deleteClip(int postId, int userIdxByJwt){
        String deleteClipQuery = "delete from CLIP where POST_ID = ? and USER_ID = ?";
        Object[] deleteClipParams = new Object[]{postId, userIdxByJwt};
        return this.jdbcTemplate.update(deleteClipQuery, deleteClipParams);
    }

    // 내 게시글 스크랩 조회 (최신순)
    public List<GetMyClipRes> getMyPostClipReverseChronological(int userIdxByJwt){
        String getMyPostClipReverseChronologicalQuery = "select p.ID as postId, DAILY_TITLE as dailyTitle, QNA_BACKGROUND_COLOR as qnaBackgroundColor, p.FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, q.CONTENTS as qnaQuestion, pp.URL as dailyImage, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST as p LEFT JOIN QUESTION as q ON p.QNA_QUESTION_ID = q.ID LEFT JOIN POST_PHOTO as pp ON p.ID = pp.POST_ID where p.ID in (select POST_ID from CLIP where USER_ID = ? and WRITER_ID = ?) ORDER BY p.CREATED_AT desc";
        return this.jdbcTemplate.query(getMyPostClipReverseChronologicalQuery,
                (rs, rowNum) -> new GetMyClipRes(
                    rs.getInt("postId"),
                    rs.getString("dailyTitle"),
                    rs.getString("qnaBackgroundColor"),
                    rs.getString("filterId"),
                    rs.getString("qnaQuestionId"),
                    rs.getString("qnaQuestion"),
                    rs.getString("dailyImage"),
                    rs.getString("qnaQuestionMadeFromUser")),
                userIdxByJwt, userIdxByJwt);
    }

    // 내 게시글 스크랩 조회 (시간순)
    public List<GetMyClipRes> getMyPostClipChronological(int userIdxByJwt){
        String getMyPostClipChronologicalQuery = "select p.ID as postId, DAILY_TITLE as dailyTitle, QNA_BACKGROUND_COLOR as qnaBackgroundColor, p.FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, q.CONTENTS as qnaQuestion, pp.URL as dailyImage, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST as p LEFT JOIN QUESTION as q ON p.QNA_QUESTION_ID = q.ID LEFT JOIN POST_PHOTO as pp ON p.ID = pp.POST_ID where p.ID in (select POST_ID from CLIP where USER_ID = ? and WRITER_ID = ?) ORDER BY p.CREATED_AT";
        return this.jdbcTemplate.query(getMyPostClipChronologicalQuery,
                (rs, rowNum) -> new GetMyClipRes(
                    rs.getInt("postId"),
                    rs.getString("dailyTitle"),
                    rs.getString("qnaBackgroundColor"),
                    rs.getString("filterId"),
                    rs.getString("qnaQuestionId"),
                    rs.getString("qnaQuestion"),
                    rs.getString("dailyImage"),
                    rs.getString("qnaQuestionMadeFromUser")),
                userIdxByJwt, userIdxByJwt);
    }

    // 타인 게시글 스크랩 조회 (최신순)
    public List<GetMyClipRes> getOtherPostClipReverseChronological(int userIdxByJwt){
        String getOtherPostClipReverseChronologicalQuery = "select p.ID as postId, DAILY_TITLE as dailyTitle, QNA_BACKGROUND_COLOR as qnaBackgroundColor, p.FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, q.CONTENTS as qnaQuestion, pp.URL as dailyImage, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST as p LEFT JOIN QUESTION as q ON p.QNA_QUESTION_ID = q.ID LEFT JOIN POST_PHOTO as pp ON p.ID = pp.POST_ID where p.ID in (select POST_ID from CLIP where USER_ID = ? and WRITER_ID != ?) ORDER BY p.CREATED_AT desc";
        return this.jdbcTemplate.query(getOtherPostClipReverseChronologicalQuery,
                (rs, rowNum) -> new GetMyClipRes(
                    rs.getInt("postId"),
                    rs.getString("dailyTitle"),
                    rs.getString("qnaBackgroundColor"),
                    rs.getString("filterId"),
                    rs.getString("qnaQuestionId"),
                    rs.getString("qnaQuestion"),
                    rs.getString("dailyImage"),
                    rs.getString("qnaQuestionMadeFromUser")),
                userIdxByJwt, userIdxByJwt);
    }

    // 타인 게시글 스크랩 조회 (시간순)
    public List<GetMyClipRes> getOtherPostClipChronological(int userIdxByJwt){
        String getOtherPostClipChronologicalQuery = "select p.ID as postId, DAILY_TITLE as dailyTitle, QNA_BACKGROUND_COLOR as qnaBackgroundColor, p.FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, q.CONTENTS as qnaQuestion, pp.URL as dailyImage, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST as p LEFT JOIN QUESTION as q ON p.QNA_QUESTION_ID = q.ID LEFT JOIN POST_PHOTO as pp ON p.ID = pp.POST_ID where p.ID in (select POST_ID from CLIP where USER_ID = ? and WRITER_ID != ?) ORDER BY p.CREATED_AT";
        return this.jdbcTemplate.query(getOtherPostClipChronologicalQuery,
                (rs, rowNum) -> new GetMyClipRes(
                    rs.getInt("postId"),
                    rs.getString("dailyTitle"),
                    rs.getString("qnaBackgroundColor"),
                    rs.getString("filterId"),
                    rs.getString("qnaQuestionId"),
                    rs.getString("qnaQuestion"),
                    rs.getString("dailyImage"),
                    rs.getString("qnaQuestionMadeFromUser")),
                userIdxByJwt, userIdxByJwt);
    }

    // 스크랩북의 내 게시글 개수 
    public GetMyPostOfClipCountRes getMyPostOfClipCount(int userIdxByJwt) {
        String getMyPostOfClipCountQuery = "select count(*) as myPostOfClipCount from CLIP where USER_ID = ? and WRITER_ID = ?";
        int getMyPostOfClipCountParam = userIdxByJwt;

        return this.jdbcTemplate.queryForObject(getMyPostOfClipCountQuery, 
            (rs,rowNum) -> new GetMyPostOfClipCountRes(
                rs.getInt("myPostOfClipCount")),
            getMyPostOfClipCountParam, getMyPostOfClipCountParam);
    }


    // 스크랩북의 타인 게시글 개수 
    public GetOtherPostOfClipCountRes getOtherPostOfClipCount(int userIdxByJwt) {
        String getOtherPostOfClipCountQuery = "select count(*) as otherPostOfClipCount from CLIP where USER_ID = ? and WRITER_ID != ?";
        int getOtherPostOfClipCountParam = userIdxByJwt;

        return this.jdbcTemplate.queryForObject(getOtherPostOfClipCountQuery, 
            (rs,rowNum) -> new GetOtherPostOfClipCountRes(
                rs.getInt("otherPostOfClipCount")),
            getOtherPostOfClipCountParam, getOtherPostOfClipCountParam);
    }

    // public List<GetUserRes> getUsersByEmail(String email){
    //     String getUsersByEmailQuery = "select * from UserInfo where email =?";
    //     String getUsersByEmailParams = email;
    //     return this.jdbcTemplate.query(getUsersByEmailQuery,
    //             (rs, rowNum) -> new GetUserRes(
    //                     rs.getInt("userIdx"),
    //                     rs.getString("userName"),
    //                     rs.getString("ID"),
    //                     rs.getString("Email"),
    //                     rs.getString("password")),
    //             getUsersByEmailParams);
    // }
    

    // public int createUser(PostUserReq postUserReq){
    //     String createUserQuery = "insert into UserInfo (userName, ID, password, email) VALUES (?,?,?,?)";
    //     Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getId(), postUserReq.getPassword(), postUserReq.getEmail()};
    //     this.jdbcTemplate.update(createUserQuery, createUserParams);

    //     String lastInserIdQuery = "select last_insert_id()";
    //     return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    // }

    // public int checkEmail(String email){
    //     String checkEmailQuery = "select exists(select email from UserInfo where email = ?)";
    //     String checkEmailParams = email;
    //     return this.jdbcTemplate.queryForObject(checkEmailQuery,
    //             int.class,
    //             checkEmailParams);

    // }

    // public int modifyUserName(PatchUserReq patchUserReq){
    //     String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
    //     Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

    //     return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    // }

    // public User getPwd(PostLoginReq postLoginReq){
    //     String getPwdQuery = "select userIdx, password,email,userName,ID from UserInfo where ID = ?";
    //     String getPwdParams = postLoginReq.getId();

    //     return this.jdbcTemplate.queryForObject(getPwdQuery,
    //             (rs,rowNum)-> new User(
    //                     rs.getInt("userIdx"),
    //                     rs.getString("ID"),
    //                     rs.getString("userName"),
    //                     rs.getString("password"),
    //                     rs.getString("email")
    //             ),
    //             getPwdParams
    //             );

    // }


}
