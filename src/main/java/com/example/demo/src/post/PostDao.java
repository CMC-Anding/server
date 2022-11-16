package com.example.demo.src.post;

import com.example.demo.src.post.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
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

    // 게시글 삭제 API
    public void deletePost(int postId){
        String deletePostQuery = "update POST SET STATUS = 'DELETED' WHERE ID = ?";
        int deletePostParams = postId;
        this.jdbcTemplate.update(deletePostQuery, deletePostParams);

        String deleteClipQuery = "update CLIP SET STATUS = 'DELETED' WHERE POST_ID = ?";
        int deleteClipParams = postId;
        this.jdbcTemplate.update(deleteClipQuery, deleteClipParams);

        String deleteAutobiographyQuery = "update AUTOBIOGRAPHY_POST SET STATUS = 'DELETED' WHERE POST_ID = ?";
        int deleteAutobiographyParams = postId;
        this.jdbcTemplate.update(deleteAutobiographyQuery, deleteAutobiographyParams);
    }

    // 일상 게시글의 사진 삭제
    public void deletePhotoOfDailyPost(int postId){
        String deletePhotoOfDailyPostQuery = "delete from POST_PHOTO where POST_ID = ?";
        int deletePhotoOfDailyPostParams = postId;
        this.jdbcTemplate.update(deletePhotoOfDailyPostQuery, deletePhotoOfDailyPostParams);
    }

    // 게시글 신고 7회 이상시, 삭제
    public void deletePostWhenReporting(int postId){
        String deletePostWhenReportingQuery = "update POST SET STATUS = 'REPORTED' WHERE ID = ?";
        int deletePostWhenReportingParams = postId;
        this.jdbcTemplate.update(deletePostWhenReportingQuery, deletePostWhenReportingParams);

        String deleteClipWhenReportingQuery = "update CLIP SET STATUS = 'REPORTED' WHERE POST_ID = ?";
        int deleteClipWhenReportingParams = postId;
        this.jdbcTemplate.update(deleteClipWhenReportingQuery, deleteClipWhenReportingParams);

        String deleteAutobiographyWhenReportingQuery = "update AUTOBIOGRAPHY_POST SET STATUS = 'REPORTED' WHERE POST_ID = ?";
        int deleteAutobiographyWhenReportingParams = postId;
        this.jdbcTemplate.update(deleteAutobiographyWhenReportingQuery, deleteAutobiographyWhenReportingParams);
    }

    // 게시글 신고 7회 이상시, 일상 게시글의 사진 삭제
    public void deletePhotoOfDailyPostWhenReporting(int postId){
        String deletePhotoOfDailyPostWhenReportingQuery = "update POST_PHOTO SET STATUS = 'REPORTED' WHERE POST_ID = ?";
        int deletePhotoOfDailyPostWhenReportingParams = postId;
        this.jdbcTemplate.update(deletePhotoOfDailyPostWhenReportingQuery, deletePhotoOfDailyPostWhenReportingParams);
    }
    
    // 일상 게시글인지, 문답 게시글인지 확인
    public CheckDailyPostOrQnaPostRes checkDailyPostOrQnaPost(int postId) {
        String checkDailyPostOrQnaPostQuery = "select if(FILTER_ID = 'e' , 'Daily', 'Qna') as dailyOrQna from POST where ID = ?";
        int checkDailyPostOrQnaPostParams = postId;

        return this.jdbcTemplate.queryForObject(checkDailyPostOrQnaPostQuery,
                (rs, rowNum) -> new CheckDailyPostOrQnaPostRes(
                    rs.getString("dailyOrQna")),
                checkDailyPostOrQnaPostParams);
    }

    // 글 상세보기 API
    public GetPostDetailRes getPostDetail(int postId){
        String getPostDetailQuery = "select u.NICKNAME as nickname, CONTENTS as contents , DAILY_TITLE as dailyTitle, QNA_BACKGROUND_COLOR as qnaBackgroundColor, p.FILTER_ID as filterId, p.QNA_QUESTION_ID as qnaQuestionId, (select CONTENTS FROM QUESTION as q WHERE q.ID = (select QNA_QUESTION_ID FROM POST as p WHERE p.ID =?)) as qnaQuestion,(select URL FROM POST_PHOTO WHERE POST_ID = ?) as dailyImage, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser, p.STATUS as status ,p.CREATED_AT as createdAt FROM POST as p left join USER as u on p.USER_ID = u.ID where p.ID = ?";
        return this.jdbcTemplate.queryForObject(getPostDetailQuery,
                (rs, rowNum) -> new GetPostDetailRes(
                        rs.getString("nickname"),
                        rs.getString("contents"),
                        rs.getString("dailyTitle"),
                        rs.getString("qnaBackgroundColor"),
                        rs.getString("filterId"),
                        rs.getString("qnaQuestionId"),
                        rs.getString("qnaQuestion"),
                        rs.getString("dailyImage"),
                        rs.getString("qnaQuestionMadeFromUser"),
                        rs.getString("status"),
                        rs.getDate("createdAt")),
                    postId, postId, postId);
    }
    
    // 스크랩 API
    public int postClip(int userIdxByJwt, int postId){
        String postClipQuery = "insert into CLIP(USER_ID, WRITER_ID, POST_ID) values (?,(select USER_ID from POST where ID = ?),?)";
        Object[] postClipParams = new Object[]{userIdxByJwt, postId, postId};
        return this.jdbcTemplate.update(postClipQuery, postClipParams);
    }

    // 스크랩 중복 확인 
    public ClipDuplicateCheckRes clipDuplicateCheck(int userIdxByJwt, int postId){
        String clipDuplicateCheckQuery = "select count(ID) as clipCount from CLIP where USER_ID = ? and POST_ID = ?";
        Object[] clipDuplicateCheckParams = new Object[]{userIdxByJwt, postId};
        return this.jdbcTemplate.queryForObject(clipDuplicateCheckQuery,
                (rs, rowNum) -> new ClipDuplicateCheckRes(
                    rs.getInt("clipCount")),
                clipDuplicateCheckParams);
    }

    // 스크랩북의 게시글 삭제 API
    public void deletePostsOfClipBook(int userIdxByJwt, DeletePostsOfClipBookReq deletePostsOfClipBookReq){
        for (int i=0 ; i < deletePostsOfClipBookReq.getPostId().size(); i++) {
        String deletePostsOfClipBookQuery = "delete from CLIP where POST_ID =? and USER_ID = ?";
        Object[] deletePostsOfClipBookParams = new Object[]{deletePostsOfClipBookReq.getPostId().get(i), userIdxByJwt};
        this.jdbcTemplate.update(deletePostsOfClipBookQuery, deletePostsOfClipBookParams);
    }
    }

    // 내 게시글 스크랩 조회 (최신순) API
    public List<GetMyClipRes> getMyPostClipReverseChronological(int userIdxByJwt){
        String getMyPostClipReverseChronologicalQuery = "select p.ID as postId, DAILY_TITLE as dailyTitle, QNA_BACKGROUND_COLOR as qnaBackgroundColor, p.FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, q.CONTENTS as qnaQuestion, pp.URL as dailyImage, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST as p LEFT JOIN QUESTION as q ON p.QNA_QUESTION_ID = q.ID LEFT JOIN POST_PHOTO as pp ON p.ID = pp.POST_ID where p.ID in (select POST_ID from CLIP where USER_ID = ? and WRITER_ID = ?) and p.STATUS = 'ACTIVE' ORDER BY p.CREATED_AT desc";
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

    // 내 게시글 스크랩 조회 (시간순) API
    public List<GetMyClipRes> getMyPostClipChronological(int userIdxByJwt){
        String getMyPostClipChronologicalQuery = "select p.ID as postId, DAILY_TITLE as dailyTitle, QNA_BACKGROUND_COLOR as qnaBackgroundColor, p.FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, q.CONTENTS as qnaQuestion, pp.URL as dailyImage, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST as p LEFT JOIN QUESTION as q ON p.QNA_QUESTION_ID = q.ID LEFT JOIN POST_PHOTO as pp ON p.ID = pp.POST_ID where p.ID in (select POST_ID from CLIP where USER_ID = ? and WRITER_ID = ?) and p.STATUS = 'ACTIVE' ORDER BY p.CREATED_AT";
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

    // 타인 게시글 스크랩 조회 (최신순) API
    public List<GetMyClipRes> getOtherPostClipReverseChronological(int userIdxByJwt){
        String getOtherPostClipReverseChronologicalQuery = "select p.ID as postId, DAILY_TITLE as dailyTitle, QNA_BACKGROUND_COLOR as qnaBackgroundColor, p.FILTER_ID as filterId, QNA_QUESTION_ID as qnaQuestionId, q.CONTENTS as qnaQuestion, pp.URL as dailyImage, QNA_QUESTION_MADE_FROM_USER as qnaQuestionMadeFromUser FROM POST as p LEFT JOIN QUESTION as q ON p.QNA_QUESTION_ID = q.ID LEFT JOIN POST_PHOTO as pp ON p.ID = pp.POST_ID where p.ID in (select POST_ID from CLIP where USER_ID = ? and WRITER_ID != ?) and p.STATUS = 'ACTIVE' ORDER BY p.CREATED_AT desc";
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

    // 타인 게시글 스크랩 조회 (시간순) API
    public List<GetMyClipRes> getOtherPostClipChronological(int userIdxByJwt) {
        String getOtherPostClipChronologicalQuery = "select p.ID                        as postId,\n" +
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
                "where p.ID in (select POST_ID from CLIP where USER_ID = ? and WRITER_ID != ?)\n" +
                "  and p.STATUS = 'ACTIVE'\n" +
                "  and p.USER_ID not in (select bu.BLOCKED_USER_ID\n" +
                "                        from USER u\n" +
                "                                 join BLOCK_USER bu on u.ID = bu.USER_ID\n" +
                "                        where u.ID = ?)\n" +
                "  and p.ID not in (select POST_ID from HIDDEN_POST where USER_ID = ?)\n" +
                "ORDER BY p.CREATED_AT;";

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
                userIdxByJwt,userIdxByJwt,userIdxByJwt,userIdxByJwt);
    }

    // 스크랩북의 내 게시글 개수 API
    public GetMyPostOfClipCountRes getMyPostOfClipCount(int userIdxByJwt) {
        String getMyPostOfClipCountQuery = "select count(*) as myPostOfClipCount from CLIP where USER_ID = ? and WRITER_ID = ? and STATUS = 'ACTIVE'";
        int getMyPostOfClipCountParam = userIdxByJwt;

        return this.jdbcTemplate.queryForObject(getMyPostOfClipCountQuery, 
            (rs,rowNum) -> new GetMyPostOfClipCountRes(
                rs.getInt("myPostOfClipCount")),
            getMyPostOfClipCountParam, getMyPostOfClipCountParam);
    }


    // 스크랩북의 타인 게시글 개수 API
    public GetOtherPostOfClipCountRes getOtherPostOfClipCount(int userIdxByJwt) {
        String getOtherPostOfClipCountQuery = "select count(*) as otherPostOfClipCount from CLIP where USER_ID = ? and WRITER_ID != ? and STATUS = 'ACTIVE' and POST_ID not in (select POST_ID from HIDDEN_POST where USER_ID = ?)";
        int getOtherPostOfClipCountParam = userIdxByJwt;

        return this.jdbcTemplate.queryForObject(getOtherPostOfClipCountQuery, 
            (rs,rowNum) -> new GetOtherPostOfClipCountRes(
                rs.getInt("otherPostOfClipCount")),
            getOtherPostOfClipCountParam, getOtherPostOfClipCountParam, userIdxByJwt);
    }

    // 신고 항목 조회 API
    public List<GetReportReasonRes> getReportReason() {
        String getReportReasonQuery = "select ID as reasonId , CONTENTS as reasonContents from REPORT_REASON";

        return this.jdbcTemplate.query(getReportReasonQuery, 
            (rs,rowNum) -> new GetReportReasonRes(
                rs.getInt("reasonId"),
                rs.getString("reasonContents")));
    }

    // 게시글 신고하기 API
    public int reportPost(int userIdxByJwt,ReportPostReq reportPostReq) {
        String reportPostQuery = "insert REPORT_LIST (USER_ID, REASON_ID, POST_ID) values (?,?,?)";
        Object[] reportPostParams = new Object[]{userIdxByJwt, reportPostReq.getReasonId(), reportPostReq.getPostId()};
        this.jdbcTemplate.update(reportPostQuery, reportPostParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    // 게시글 총 신고횟수 체크
    public CheckReportCountForDeleteRes getTotalReportCount(ReportPostReq reportPostReq) {
        String getTotalReportCountQuery = "select if(count(*)>6, '삭제조건도달', '삭제조건미달') as shouldRemoveOrNot from REPORT_LIST where POST_ID = ?";
        int getTotalReportCountParam = reportPostReq.getPostId();

        return this.jdbcTemplate.queryForObject(getTotalReportCountQuery, 
            (rs,rowNum) -> new CheckReportCountForDeleteRes (
                rs.getString("shouldRemoveOrNot")),
            getTotalReportCountParam);
    }

    // 게시글 작성자 ID 조회
    public GetPostWriterIdRes getWriterId(int postId) {
        String getPostWriterIdQuery = "select USER_ID as writerId from POST where ID= ?";
        int getPostWriterIdParams = postId;
        return this.jdbcTemplate.queryForObject(getPostWriterIdQuery, 
            (rs,rowNum) -> new GetPostWriterIdRes(
                rs.getInt("writerId")),
            getPostWriterIdParams);
    }

    // 일상 게시글 수정 (사진 제외)
    public void updateDailyPost(PostDailyPostReq postDailyPostReq, int postId){
        String updateDailyPostQuery = "update POST set USER_ID = ? , FILTER_ID = 'e', DAILY_TITLE = ?, CONTENTS = ?, FEED_SHARE = ? where ID = ?";
        Object[] updateDailyPostParams = new Object[]{postDailyPostReq.getUserId(), postDailyPostReq.getDailyTitle(), postDailyPostReq.getContents(), postDailyPostReq.getFeedShare(), postId};
        this.jdbcTemplate.update(updateDailyPostQuery,updateDailyPostParams);
    }

    // 문답 게시글 수정
    public void updateQnaPost(PostQnaPostReq postQnaPostReq, int postId){
        String updateQnaPostQuery = "update POST set USER_ID = ? , FILTER_ID = ?, QNA_QUESTION_ID = ?, CONTENTS= ?, QNA_BACKGROUND_COLOR = ?, FEED_SHARE = ? where ID = ?";
        Object[] updateQnaPostParams = new Object[]{postQnaPostReq.getUserId(), postQnaPostReq.getFilterId(), postQnaPostReq.getQnaQuestionId(), postQnaPostReq.getContents(), postQnaPostReq.getQnaBackgroundColor(), postQnaPostReq.getFeedShare(), postId};
        this.jdbcTemplate.update(updateQnaPostQuery,updateQnaPostParams);
    }

    /* 
     * 게시글 수정 
     * 일상 게시글의 사진 수정
    */
    public void updateImage(String url, int postId){
        String createImageQuery = "update POST_PHOTO set URL = ? where POST_ID = ?";
        Object[] createImageParams = new Object[]{url, postId};
        this.jdbcTemplate.update(createImageQuery, createImageParams);
    }

    // 일상 게시글 이미지 url 조회
    public GetImageUrlRes getImageUrl(int postId) {
        String getImageUrlQuery = "select URL as imageUrl from POST_PHOTO where POST_ID = ?";
        int getImageUrlParams = postId;
        return this.jdbcTemplate.queryForObject(getImageUrlQuery, 
            (rs,rowNum) -> new GetImageUrlRes(
                rs.getString("imageUrl")),
            getImageUrlParams);
    }

    //게시글 가리기
    public void hidingPost(int userIdxByJwt, int postId) {
        String hidingPostQuery = "insert into HIDDEN_POST(USER_ID, POST_ID) values (?,?)";
        Object[] hidingPostParams = new Object[]{userIdxByJwt, postId};
        this.jdbcTemplate.update(hidingPostQuery, hidingPostParams);
    }

    // //게시글 작성자 id 추출
    // public GetWriterIdRes getWriterId(int postId) {
    //     String getWriterIdQuery = "";
    //     int getWriterIdParams = postId;
    //     return this.jdbcTemplate.queryForObject(getWriterIdQuery, 
    //         (rs,rowNum) -> new GetWriterIdRes(
    //             rs.getInt("writerId")),
    //         getWriterIdParams);
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


}
