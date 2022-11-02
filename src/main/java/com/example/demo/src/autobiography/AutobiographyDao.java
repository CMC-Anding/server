package com.example.demo.src.autobiography;


import com.example.demo.src.autobiography.model.*;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AutobiographyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //자서전의 게시글구성 제외한 요소 수정
    public int modifyAutobiographyEtc(int autobiographyId, ModifyAutobiographyEtcReq modifyAutobiographyEtcReq) {
        String modifyAutobiographyEtcQuery = "update AUTOBIOGRAPHY set TITLE = ?, DETAIL = ?, COVER_COLOR = ?, TITLE_COLOR = ? where ID = ?";
        Object[] modifyAutobiographyEtcParams = new Object[] {modifyAutobiographyEtcReq.getTitle(), modifyAutobiographyEtcReq.getDetail(), modifyAutobiographyEtcReq.getCoverColor(), modifyAutobiographyEtcReq.getTitleColor(), autobiographyId};
        return this.jdbcTemplate.update(modifyAutobiographyEtcQuery, modifyAutobiographyEtcParams);
    }

    //자서전 게시글구성 수정시 기존 구성 선 삭제
    public int deleteAutobiographyPost(int autobiographyId) {
        String deleteAutobiographyPostQuery = "delete from AUTOBIOGRAPHY_POST where AUTOBIOGRAPHY_ID = ?";
        int deleteAutobiographyPostParams = autobiographyId;
        return this.jdbcTemplate.update(deleteAutobiographyPostQuery, deleteAutobiographyPostParams);
    }

    //자서전 게시글구성 수정 새로운 구성 생성
    public void insertAutobiographyPost(int autobiographyId, ModifyAutobiographyPostReq modifyAutobiographyPostReq) {
        for (int i=0 ; i < modifyAutobiographyPostReq.getPostId().size(); i++) {
        String insertAutobiographyPostQuery = "insert into AUTOBIOGRAPHY_POST(AUTOBIOGRAPHY_ID, POST_ID, PAGE_ORDER) values (?, ?, ?)";
        Object[] insertAutobiographyPostParams = new Object[] {autobiographyId, modifyAutobiographyPostReq.getPostId().get(i), i+1};
        this.jdbcTemplate.update(insertAutobiographyPostQuery, insertAutobiographyPostParams);
        }
        
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

    /* 자서전 틀 생성*/
    public int createAutobiography(int userId, PostAutobiographyReq postAutobiographyReq) {
        String query = "insert into AUTOBIOGRAPHY(USER_ID, TITLE, DETAIL, COVER_COLOR, TITLE_COLOR) values (?, ?, ?, ?, ?)";
        Object[] params = new Object[]{userId, postAutobiographyReq.getTitle(), postAutobiographyReq.getDetail(), postAutobiographyReq.getCoverColor(), postAutobiographyReq.getTitleColor()};

        this.jdbcTemplate.update(query, params);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);

    }


    /* 자서전에 게시글들 연결*/
    public int connectPostsToAutobiography(int autobiographyId, PostAutobiographyReq postAutobiographyReq) {
        String query = "insert into AUTOBIOGRAPHY_POST (AUTOBIOGRAPHY_ID, POST_ID, PAGE_ORDER) VALUES (?,?,?) ";
        return this.jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, autobiographyId);
                ps.setInt(2, postAutobiographyReq.getPostIds().get(i));
                ps.setInt(3, i+1);
            }
            @Override
            public int getBatchSize() {return postAutobiographyReq.getPostIds().size();}
        }).length;
    }

    /* 자서전 목록 조회 */
    public List<Autobiography> getMyAutobiographyList(int userId) {
        String query = "SELECT ID, TITLE, DETAIL, COVER_COLOR, TITLE_COLOR, CREATED_AT\n" +
                "FROM AUTOBIOGRAPHY\n" +
                "WHERE USER_ID=?";

        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new Autobiography(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("DETAIL"),
                        rs.getString("COVER_COLOR"),
                        rs.getString("TITLE_COLOR"),
                        rs.getString("CREATED_AT")
                ),
                userId);
    }

    /* 자서전 보기 */
    public PostDetail getMyAutobiographyPage(int autobiographyId, int page) {
        String query = "SELECT P.FILTER_ID AS FILTER_ID, QNA_BACKGROUND_COLOR, QNA_QUESTION_ID, Q.CONTENTS AS QNA_QUESTION, QNA_QUESTION_MADE_FROM_USER, P.CONTENTS AS CONTENTS, DAILY_TITLE, URL AS DAILY_IMAGE, P.CREATED_AT AS CREATED_AT, LAST_PAGE\n" +
                "FROM AUTOBIOGRAPHY_POST AP\n" +
                "JOIN POST P on AP.POST_ID = P.ID\n" +
                "LEFT JOIN QUESTION Q on P.QNA_QUESTION_ID = Q.ID\n" +
                "LEFT JOIN POST_PHOTO PP on P.ID = PP.POST_ID\n" +
                "JOIN (SELECT AUTOBIOGRAPHY_ID, MAX(PAGE_ORDER) AS LAST_PAGE\n" +
                "FROM AUTOBIOGRAPHY_POST AP\n" +
                "GROUP BY AUTOBIOGRAPHY_ID) AS LP on AP.AUTOBIOGRAPHY_ID=LP.AUTOBIOGRAPHY_ID\n" +
                "WHERE AP.AUTOBIOGRAPHY_ID=? AND AP.PAGE_ORDER=?;";
        Object[] params = new Object[]{autobiographyId,page};

        return this.jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new PostDetail(
                        rs.getString("FILTER_ID"),
                        rs.getString("QNA_BACKGROUND_COLOR"),
                        rs.getString("QNA_QUESTION_ID"),
                        rs.getString("QNA_QUESTION"),
                        rs.getString("QNA_QUESTION_MADE_FROM_USER"),
                        rs.getString("CONTENTS"),
                        rs.getString("DAILY_TITLE"),
                        rs.getString("DAILY_IMAGE"),
                        rs.getString("CREATED_AT"),
                        rs.getInt("LAST_PAGE")
                ),
                params);
    }

    /* 내가 선물한 자서전 개수 조회*/
    public int getNumberOfAutographiesGiftedFromMe(int userId) {
        String query = "SELECT COUNT(ID) AS NUMBER_OF_GIFTED_AUTOGRAPHIES_FROM_ME\n" +
                "FROM GIFT\n" +
                "WHERE USER_ID=?";

        return this.jdbcTemplate.queryForObject(query,int.class,userId);
    }
}
