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

    public int postPost(PostPostReq postPostReq){
        String postPostQuery = "insert into POST (USER_ID, CONTENTS, DAILY_TITLE, QNA_BACKGROUND_COLOR, FILTER_ID, QNA_QUESTION_ID, QUESTION_MADE_FROM_USER) VALUES (?,?,?,?,?,?,?)";
        Object[] postPostParams = new Object[]{postPostReq.getUserId(), postPostReq.getContents(), postPostReq.getDaily_title(), postPostReq.getQnaBackgroundColor(), postPostReq.getFilterId(), postPostReq.getQnaQuestionId(), postPostReq.getQuestionMadeFromUser()};
        this.jdbcTemplate.update(postPostQuery, postPostParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public void createImage(String url, int postId){
        String createImageQuery = "insert into POST_PHOTO (URL, POST_ID) values (?, ?)";
        Object[] createImageParams = new Object[]{url, postId};
        this.jdbcTemplate.update(createImageQuery, createImageParams);
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

    public GetPostDetailRes getPostDetail(int postId){
        String getPostDetailQuery = "select * from UserInfo where userIdx = ?";
        return this.jdbcTemplate.queryForObject(getPostDetailQuery,
                (rs, rowNum) -> new GetPostDetailRes(
                        rs.getString("contents"),
                        rs.getString("dailyTitle"),
                        rs.getString("qnaBackgroundColor"),
                        rs.getString("qnaFilterId"),
                        rs.getString("qnaQuestionId"),
                        rs.getString("qnaQuestion"),
                        rs.getString("dailyImage"),
                        rs.getString("qnaQuestionMadeFromUser")),
                    postId);
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
