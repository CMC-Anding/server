package com.example.demo.src.autobiography;


import com.example.demo.src.autobiography.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
        for (int i=0 ; i<modifyAutobiographyPostReq.getPostId().length; i++) {
            String insertAutobiographyPostQuery = "insert into AUTOBIOGRAPHY_POST(POST_ID, PAGE_ORDER) values(?,?) where AUTOBIOGRAPHY_ID =?";
            Object[] insertAutobiographyPostParams = new Object[] {modifyAutobiographyPostReq.getPostId()[i], i+1, autobiographyId};
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


}
