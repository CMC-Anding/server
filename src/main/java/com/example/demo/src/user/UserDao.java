package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from UserInfo";

        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;

        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from UserInfo where userIdx = ?";
        int getUserParams = userIdx;

        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUserParams);
    }
    

    /* 회원가입 */
    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into USER (NICKNAME, LOGIN_ID, LOGIN_PASSWORD, PHONE_NUMBER) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getNickname(), postUserReq.getUserId(), postUserReq.getPassword(), postUserReq.getPhone()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";

        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getUser(PostLoginReq postLoginReq){
        String query = "SELECT ID, LOGIN_ID, LOGIN_PASSWORD, NICKNAME, PHONE_NUMBER FROM USER where LOGIN_ID = ?";

        return this.jdbcTemplate.queryForObject(query,
                (rs,rowNum)-> new User(
                        rs.getInt("ID"),
                        rs.getString("LOGIN_ID"),
                        rs.getString("LOGIN_PASSWORD"),
                        rs.getString("NICKNAME"),
                        rs.getString("PHONE_NUMBER")
                ),
                postLoginReq.getUserId()
                );
    }

    /* 사용자 아이디 중복 검사 */
    public int checkUserId(String userId) {
        String query = "SELECT EXISTS(SELECT LOGIN_ID FROM USER WHERE LOGIN_ID = ?)";

        return this.jdbcTemplate.queryForObject(query,int.class,userId);

    }

    /* 닉네임 중복 검사 */
    public int checkNickname(String nickname) {
        String query = "SELECT EXISTS(SELECT NICKNAME FROM USER WHERE NICKNAME = ?)";

        return this.jdbcTemplate.queryForObject(query,int.class,nickname);
    }

    public int checkPhoneNumber(String phoneNumber) {
        String query = "SELECT EXISTS(SELECT PHONE_NUMBER FROM USER WHERE PHONE_NUMBER = ?)";

        return this.jdbcTemplate.queryForObject(query,int.class,phoneNumber);
    }

    public void modifyUserProfile(int userIdx, UserProfile userProfile, MultipartFile image) {
        String query = "update USER set NICKNAME = ?";

        if (userProfile.getIntroduction() != null) {
            query+=", INTRODUCTION = ?";
        }
        if (image != null) {
            query+=", image = ?";
        }
        query+=" where ID = ?";

        Object[] params = new Object[]{userProfile.getNickname(), userProfile.getIntroduction(), userIdx};

        this.jdbcTemplate.update(query,params);
    }

    public UserProfile getUserProfile(int userId) {
        String query = "SELECT PROFILE_PHOTO, NICKNAME, INTRODUCTION FROM USER WHERE ID = ?";

        return this.jdbcTemplate.queryForObject(query,
                (rs,rowNum)-> new UserProfile(
                        rs.getString("PROFILE_PHOTO"),
                        rs.getString("NICKNAME"),
                        rs.getString("INTRODUCTION")
                ),
                userId);
    }
}
