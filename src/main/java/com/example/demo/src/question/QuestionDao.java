package com.example.demo.src.question;


import com.example.demo.src.question.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class QuestionDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
     * 각 필터의 질문 가져오기 API
     */
    public GetQuestionRes getQuestion(String filterId, int userIdxByJwt){
        String getQuestionQuery = "select CONTENTS as question, COUNT(*) as numberOfRemaning from QUESTION where FILTER_ID = ? and ID NOT IN (select QNA_QUESTION_ID from POST where USER_ID =? and FILTER_ID = ?) ORDER BY RAND() LIMIT 1";
        return this.jdbcTemplate.queryForObject(getQuestionQuery,
                (rs,rowNum) -> new GetQuestionRes(
                        rs.getString("question"),
                        rs.getInt("numberOfRemaning")),
                filterId, userIdxByJwt, filterId);
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

    // public GetUserRes getUser(int userIdx){
    //     String getUserQuery = "select * from UserInfo where userIdx = ?";
    //     int getUserParams = userIdx;
    //     return this.jdbcTemplate.queryForObject(getUserQuery,
    //             (rs, rowNum) -> new GetUserRes(
    //                     rs.getInt("userIdx"),
    //                     rs.getString("userName"),
    //                     rs.getString("ID"),
    //                     rs.getString("Email"),
    //                     rs.getString("password")),
    //             getUserParams);
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
