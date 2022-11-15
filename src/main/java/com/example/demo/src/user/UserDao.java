package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    /* 프로필 수정 */
    public void modifyUserProfile(int userId, PatchUserProfileReq patchUserProfileReq, String imageUrl) {
        String query = "update USER set ";
        boolean isPreviousColumnExist = false;

        if (!Objects.isNull(patchUserProfileReq.getNickname())) {
            query += " NICKNAME = " + "'"+patchUserProfileReq.getNickname()+"'";
            isPreviousColumnExist = true;
        }

        if (!Objects.isNull(patchUserProfileReq.getIntroduction())) {
            if (isPreviousColumnExist) {
                query += ",";
            }
            query += " INTRODUCTION = " +"'"+patchUserProfileReq.getIntroduction()+"'";
            isPreviousColumnExist = true;
        }

        if (!Objects.isNull(imageUrl)) {
            if (isPreviousColumnExist) {
                query += ",";
            }
            query+=" PROFILE_PHOTO= "+"'"+imageUrl+"'";
            isPreviousColumnExist = true;
        } else if (patchUserProfileReq.isImageDeleted()) {
            if (isPreviousColumnExist) {
                query += ",";
            }
            query+=" PROFILE_PHOTO= null";
            isPreviousColumnExist = true;
        }

        query+=" \n where ID = ?";


        this.jdbcTemplate.update(query,userId);
    }

    /* 프로필 조회 */
    public UserProfile getUserProfile(int userId) {
        String query = "SELECT PROFILE_PHOTO, NICKNAME, INTRODUCTION\n" +
                "FROM USER\n" +
                "WHERE ID = ?";

        return this.jdbcTemplate.queryForObject(query,
                (rs,rowNum)-> new UserProfile(
                        rs.getString("PROFILE_PHOTO"),
                        rs.getString("NICKNAME"),
                        rs.getString("INTRODUCTION")
                ),
                userId);
    }

    /* 나의 게시글 수 조회 */
    public int getNumberOfMyPosts(int userId) {
        String query = "SELECT COUNT(ID)\n" +
                "FROM POST\n" +
                "WHERE USER_ID = ? AND STATUS = 'ACTIVE';";

        return this.jdbcTemplate.queryForObject(query, int.class, userId);
    }

    /* 나의 자서전 개수 조회 */
    public int getNumberOfMyAutobiographies(int userId) {
        String query = "SELECT COUNT(ID)\n" +
                "FROM AUTOBIOGRAPHY\n" +
                "WHERE USER_ID = ? AND STATUS = 'ACTIVE'";

        return this.jdbcTemplate.queryForObject(query, int.class, userId);
    }

    /* 선물받은 자서전 목록 조회 */
    public List<GiftedAutobiography> getGiftedAutobiographies(int userId) {
        String query = "SELECT A.ID, TITLE, TITLE_COLOR, COVER_COLOR, DETAIL, G.CREATED_AT\n" +
                "FROM AUTOBIOGRAPHY A\n" +
                "JOIN GIFT G on A.ID = G.AUTOBIOGRAPHY_ID\n" +
                "JOIN USER U on G.GUEST_PHONE_NUMBER = U.PHONE_NUMBER\n" +
                "WHERE U.ID = ?\n" +
                "ORDER BY G.CREATED_AT DESC";

        return this.jdbcTemplate.query(query,
                (rs,rowNum)-> new GiftedAutobiography(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("TITLE_COLOR"),
                        rs.getString("COVER_COLOR"),
                        rs.getString("DETAIL"),
                        rs.getString("CREATED_AT")
                ),
                userId);
    }

    /* 회원 탈퇴 */
    public int deleteUser(int userId) {
        String query = "DELETE FROM USER WHERE ID = ? ";
        return this.jdbcTemplate.update(query,userId);
    }

    /* 사용자 차단 */
    public void blockUser(int userId, String nickname) {
        String query = "INSERT IGNORE INTO BLOCK_USER (USER_ID, BLOCKED_USER_ID) VALUES (?,\n" +
                "                                                                    (SELECT ID\n" +
                "                                                                     FROM USER\n" +
                "                                                                     WHERE NICKNAME=?)\n" +
                "                                                                 )";
        Object[] params = new Object[]{userId, nickname};

        this.jdbcTemplate.update(query,params);
    }
}
