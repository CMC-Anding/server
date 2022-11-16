package com.example.demo.src.feed;


import com.example.demo.src.feed.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FeedDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 피드의 전체 눌렀을 때 
    public List<GetFeedListRes> getFeedList(int userIdxByJwt){
        String getFeedListQuery = "select p.ID                        as postId,\n" +
                "       DAILY_TITLE                 as dailyTitle,\n" +
                "       QNA_BACKGROUND_COLOR        as qnaBackgroundColor,\n" +
                "       p.FILTER_ID                 as filterId,\n" +
                "       QNA_QUESTION_ID             as qnaQuestionId,\n" +
                "       q.CONTENTS                  as qnaQuestion,\n" +
                "       pp.URL                      as dailyImage,\n" +
                "       QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser\n" +
                "FROM POST as p\n" +
                "         LEFT JOIN QUESTION as q ON p.QNA_QUESTION_ID = q.ID\n" +
                "         LEFT JOIN POST_PHOTO as pp ON p.ID = pp.POST_ID\n" +
                "where p.FEED_SHARE = ?\n" +
                "  and p.STATUS = ?\n" +
                "  and p.ID not in (select POST_ID from HIDDEN_POST where USER_ID = ?)\n" +
                "  and p.USER_ID not in (select bu.BLOCKED_USER_ID\n" +
                "                        from USER u\n" +
                "                                 join BLOCK_USER bu on u.ID = bu.USER_ID\n" +
                "                        where u.ID = ?)\n" +
                "ORDER BY rand()";

        return this.jdbcTemplate.query(getFeedListQuery,
                (rs, rowNum) -> new GetFeedListRes(
                    rs.getInt("postId"),
                    rs.getString("dailyTitle"),
                    rs.getString("qnaBackgroundColor"),
                    rs.getString("filterId"),
                    rs.getString("qnaQuestionId"),
                    rs.getString("qnaQuestion"),
                    rs.getString("dailyImage"),
                    rs.getString("qnaQuestionMadeFromUser")),
                "Y", "ACTIVE", userIdxByJwt, userIdxByJwt);
    }

    //피드의 필터 눌렀을 때 
    public List<GetFeedListRes> getFeedListByFilterId(String filterId, int userIdxByJwt){
        String getFeedListByFilterIdQuery = "select p.ID                        as postId,\n" +
                "       DAILY_TITLE                 as dailyTitle,\n" +
                "       QNA_BACKGROUND_COLOR        as qnaBackgroundColor,\n" +
                "       p.FILTER_ID                 as filterId,\n" +
                "       QNA_QUESTION_ID             as qnaQuestionId,\n" +
                "       q.CONTENTS                  as qnaQuestion,\n" +
                "       pp.URL                      as dailyImage,\n" +
                "       QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser\n" +
                "FROM POST as p\n" +
                "         LEFT JOIN QUESTION as q ON p.QNA_QUESTION_ID = q.ID\n" +
                "         LEFT JOIN POST_PHOTO as pp ON p.ID = pp.POST_ID\n" +
                "where p.FILTER_ID = ?\n" +
                "  and p.FEED_SHARE = ?\n" +
                "  and p.STATUS = ?\n" +
                "  and p.ID not in (select POST_ID from HIDDEN_POST where USER_ID = ?)\n" +
                "  and p.USER_ID not in (select bu.BLOCKED_USER_ID\n" +
                "                        from USER u\n" +
                "                                 join BLOCK_USER bu on u.ID = bu.USER_ID\n" +
                "                        where u.ID = ?)\n" +
                "ORDER BY rand()";
        String getFeedListByFilterIdParams = filterId;
        return this.jdbcTemplate.query(getFeedListByFilterIdQuery,
                (rs, rowNum) -> new GetFeedListRes(
                    rs.getInt("postId"),
                    rs.getString("dailyTitle"),
                    rs.getString("qnaBackgroundColor"),
                    rs.getString("filterId"),
                    rs.getString("qnaQuestionId"),
                    rs.getString("qnaQuestion"),
                    rs.getString("dailyImage"),
                    rs.getString("qnaQuestionMadeFromUser")),
                getFeedListByFilterIdParams, "Y", "ACTIVE", userIdxByJwt,userIdxByJwt);
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
