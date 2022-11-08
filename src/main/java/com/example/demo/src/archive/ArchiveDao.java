package com.example.demo.src.archive;


import com.example.demo.src.archive.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ArchiveDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* 
    * 아카이브 문답 조회 API (최신순)
    * 아카이브 문답의 전체 눌렀을 때
    */
    public List<GetArchiveQnaRes> getArchiveQnaListReverseChronological(int userIdxByJwt){
        String getArchiveQnaListQuery = "select ID as postId, QNA_BACKGROUND_COLOR as qnaBackgroundColor, FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, (select CONTENTS from QUESTION where ID = p.QNA_QUESTION_ID) as qnaQuestion, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST as p where USER_ID = ? and FILTER_ID not in (?) order by p.CREATED_AT desc";
        return this.jdbcTemplate.query(getArchiveQnaListQuery,
                (rs, rowNum) -> new GetArchiveQnaRes(
                    rs.getInt("postId"),
                    rs.getString("qnaBackgroundColor"),
                    rs.getString("filterId"),
                    rs.getString("qnaQuestionId"),
                    rs.getString("qnaQuestion"),
                    rs.getString("qnaQuestionMadeFromUser")),
                userIdxByJwt, "e");
    }

    /* 
    * 아카이브 문답 조회 API (시간순)
    * 아카이브 문답의 전체 눌렀을 때
    */
    public List<GetArchiveQnaRes> getArchiveQnaListChronological(int userIdxByJwt){
        String getArchiveQnaListQuery = "select ID as postId, QNA_BACKGROUND_COLOR as qnaBackgroundColor, FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, (select CONTENTS from QUESTION where ID = p.QNA_QUESTION_ID) as qnaQuestion, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST as p where USER_ID = ? and FILTER_ID not in (?) order by p.CREATED_AT";
        return this.jdbcTemplate.query(getArchiveQnaListQuery,
                (rs, rowNum) -> new GetArchiveQnaRes(
                    rs.getInt("postId"),
                    rs.getString("qnaBackgroundColor"),
                    rs.getString("filterId"),
                    rs.getString("qnaQuestionId"),
                    rs.getString("qnaQuestion"),
                    rs.getString("qnaQuestionMadeFromUser")),
                userIdxByJwt, "e");
    }

    /* 
    * 아카이브 문답 조회 API (최신순)
    * 아카이브 문답의 개별 필터 눌렀을 때
    */
    public List<GetArchiveQnaRes> getArchiveQnaListByFilterIdReverseChronological(String filterId, int userIdxByJwt){
        String getArchiveQnaListQuery = "select ID as postId, QNA_BACKGROUND_COLOR as qnaBackgroundColor, FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, (select CONTENTS from QUESTION where ID = p.QNA_QUESTION_ID) as qnaQuestion, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST as p where USER_ID = ? and FILTER_ID in (?) order by p.CREATED_AT desc";
        return this.jdbcTemplate.query(getArchiveQnaListQuery,
                (rs, rowNum) -> new GetArchiveQnaRes(
                    rs.getInt("postId"),
                    rs.getString("qnaBackgroundColor"),
                    rs.getString("filterId"),
                    rs.getString("qnaQuestionId"),
                    rs.getString("qnaQuestion"),
                    rs.getString("qnaQuestionMadeFromUser")),
                userIdxByJwt, filterId);
    }

    /* 
    * 아카이브 문답 조회 API (시간순)
    * 아카이브 문답의 개별 필터 눌렀을 때
    */
    public List<GetArchiveQnaRes> getArchiveQnaListByFilterIdChronological(String filterId, int userIdxByJwt){
        String getArchiveQnaListQuery = "select ID as postId, QNA_BACKGROUND_COLOR as qnaBackgroundColor, FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, (select CONTENTS from QUESTION where ID = p.QNA_QUESTION_ID) as qnaQuestion, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST as p where USER_ID = ? and FILTER_ID in (?) order by p.CREATED_AT";
        return this.jdbcTemplate.query(getArchiveQnaListQuery,
                (rs, rowNum) -> new GetArchiveQnaRes(
                    rs.getInt("postId"),
                    rs.getString("qnaBackgroundColor"),
                    rs.getString("filterId"),
                    rs.getString("qnaQuestionId"),
                    rs.getString("qnaQuestion"),
                    rs.getString("qnaQuestionMadeFromUser")),
                userIdxByJwt, filterId);
    }

    //아카이브 일상 조회 API (최신순)
    public List<GetArchiveDailyRes> getArchiveDailyListReverseChronological(int userIdxByJwt){
        String getArchiveDailyListQuery = "select p.ID as postId, p.DAILY_TITLE as dailyTitle, pp.URL as dailyImage FROM POST as p left join POST_PHOTO as pp on pp.POST_ID = p.ID where p.USER_ID = ? and p.FILTER_ID = ? order by p.CREATED_AT desc";
        return this.jdbcTemplate.query(getArchiveDailyListQuery,
                (rs, rowNum) -> new GetArchiveDailyRes(
                    rs.getInt("postId"),
                    rs.getString("dailyTitle"),
                    rs.getString("dailyImage")),
                userIdxByJwt, "e");
    }

    //아카이브 일상 조회 API (시간순)
    public List<GetArchiveDailyRes> getArchiveDailyListChronological(int userIdxByJwt){
        String getArchiveDailyListQuery = "select p.ID as postId, p.DAILY_TITLE as dailyTitle, pp.URL as dailyImage FROM POST as p left join POST_PHOTO as pp on pp.POST_ID = p.ID where p.USER_ID = ? and p.FILTER_ID = ? order by p.CREATED_AT";
        return this.jdbcTemplate.query(getArchiveDailyListQuery,
                (rs, rowNum) -> new GetArchiveDailyRes(
                    rs.getInt("postId"),
                    rs.getString("dailyTitle"),
                    rs.getString("dailyImage")),
                userIdxByJwt, "e");
    }

    //아카이브 문답 게시글 개수 API
    public GetQnaPostCountRes getQnaPostCount(int userIdxByJwt) {
        String getQnaPostCountQuery = "select count(*) as qnaPostCount from POST where USER_ID = ? and FILTER_ID NOT IN (?)";
        int getQnaPostCountParam = userIdxByJwt;

        return this.jdbcTemplate.queryForObject(getQnaPostCountQuery, 
            (rs,rowNum) -> new GetQnaPostCountRes(
                rs.getInt("qnaPostCount")),
            getQnaPostCountParam, "e");
    }

    //아카이브 일상 게시글 개수 API
    public GetDailyPostCountRes getDailyPostCount(int userIdxByJwt) {
        String getDailyPostCountQuery = "select count(*) as dailyPostCount from POST where USER_ID = ? and FILTER_ID IN (?)";
        int getDailyPostCountParam = userIdxByJwt;

        return this.jdbcTemplate.queryForObject(getDailyPostCountQuery, 
            (rs,rowNum) -> new GetDailyPostCountRes(
                rs.getInt("dailyPostCount")),
            getDailyPostCountParam, "e");
    }


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
