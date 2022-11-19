package com.example.demo.config;

import lombok.Getter;

/**
 * status 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    VALID_USER_ID(true, 1001, "사용 가능한 아이디입니다."),
    VALID_NICKNAME(true, 1002, "사용 가능한 닉네임입니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),


    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    DUPLICATED_USER_ID(false, 3015, "이미 등록된 아이디입니다."),
    DUPLICATED_NICKNAME(false, 3016, "이미 등록된 닉네임입니다."),

    UNREGISTERED_PHONE_NUMBER(false, 3017, "등록되지 않은 전화번호 입니다."),
    DUPLICATED_PHONE_NUMBER(false, 3018, "이미 가입된 전화번호입니다."),
    FAILED_TO_DELETE_ACCOUNT(false,3019,"이미 삭제되었거나 존재하지 않는 계정입니다."),
    FAILED_TO_BLOCK_ACCOUNT(false,3020,"이미 차단되었거나 존재하지 않는 계정입니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    S3_UPLOAD_FAIL(false, 4002, "S3 업로드에 실패했습니다."),
    S3_DELETE_FAIL(false, 4003, "S3 객체 삭제에 실패했습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    //[GET] /app/questions/:filter-id
    GET_QUESTION_ERROR(false,4501,"사용자가 작성하지않은 질문을 가져오는데에 실패했습니다."),

    // 자서전 관련
    MODIFY_FAIL_AUTOGRAPHY_ETC(false, 4550, "자서전의 제목, 세부설명, 색상의 정보 수정에 실패했습니다."),
    DELETE_FAIL_AUTOGRAPHY_POST(false, 4551, "기존 자서전의 게시글 구성 삭제에 실패했습니다."),
    INSERT_FAIL_AUTOGRAPHY_POST(false, 4552, "자서전의 게시글 구성 생성에 실패했습니다."),

    // 스크랩 관련
    CLIP_FAIL(false, 4600,"나의 글 혹은 익명의 글을 스크랩하는데 실패했습니다."),
    CLIP_DUPLICATE_CHECK_ERROR(false,4601,"중복 스크랩 확인에 실패했습니다."),
    DELETE_CLIP_FAIL(false, 4602, "스크랩북 수정(구성 게시글 삭제)에 실패했습니다."),
    DUPLICATED_CLIP(false,4603,"이미 스크랩 한 게시글입니다."),

    // 게시글 관련
    POST_DAILY_POST_ERROR(false,4650,"일상게시글(사진 제외)을 등록하는데 실패했습니다."),
    REPORT_POST_ERROR(false,4651,"게시글 신고에 실패했습니다."),
    CHECK_DAILY_OR_REPORT_FAIL(false,4652,"일상 게시글인지, 문답 게시글인지 확인하는데 실패했습니다."),
    CHECK_REPORT_COUNT_ERROR(false, 4653, "게시글 신고횟수가 7회이상인지 확인하는데 실패했습니다."),
    POST_DELETE_ERROR(false, 4654, "일상 게시글(사진 제외) 혹은 문답 게시글 삭제에 실패했습니다."),
    PHOTO_DELETE_ERROR(false,4655,"일상 게시글의 사진 삭제에 실패했습니다."),
    POST_DELETE_WHEN_REPORTING_ERROR(false, 4656, "게시글 신고횟수가 7회이상일 때, 일상 게시글(사진 제외) 혹은 문답 게시글 삭제에 실패했습니다."),
    PHOTO_DELETE_WHEN_REPORTING_ERROR(false, 4657, "게시글 신고횟수가 7회이상일 때, 일상 게시글의 사진 삭제에 실패했습니다."),
    POST_QNA_POST_ERROR(false, 4658, "문답 게시글 등록에 실패했습니다."),
    UPDATE_DAILY_POST_FAIL(false, 4659, "일상 게시글 수정에 실패했습니다."),
    UPDATE_QNA_POST_FAIL(false, 4660, "문답 게시글 수정에 실패했습니다."),
    S3_FILE_DELETE_REQ_FAIL(false, 4661, "일상 게시글의 이미지에 대한 S3 파일 삭제요청에 실패했습니다."),
    S3_FILE_POST_REQ_FAIL(false,4662,"일상 게시글의 이미지에 대한 S3 파일 업로드요청에 실패했습니다."),
    UPDATE_IMAGE_FAIL(false, 4663, "일상 게시글의 이미지 수정에 실패했습니다."),
    GET_USER_ID_FAIL(false, 4664, "게시글의 작성자 id를 조회하는데 실패했습니다."),
    CHECK_IF_USER_WRITER_MATCH(false,4665,"게시글 작성자 id와 사용자 id가 일치하는지 확인하는데 실패했습니다."),
    HIDING_POST_FAIL(false,4666,"게시글 가리기에 실패했습니다."),
    CANNOT_HIDE_MY_POST(false,4667,"나의 게시글은 가릴 수 없습니다."),
    GET_CLIPBOOK_OTHER_POST_COUNT_FAIL(false, 4668, "스크랩북의 타인 게시글 개수를 가져오는데 실패했습니다."),
    GET_REPORT_REASON_FAIL(false,4669,"신고 항목 불러오기에 실패했습니다."),
    GET_CLIPBOOK_MY_POST_COUNT_FAIL(false,4670,"스크랩북의 나의 게시글 개수를 가져오는데 실패했습니다."),
    GET_OTHER_POST_OF_CLIPBOOK_FAIL(false,4671,"스크랩북의 타인 게시글 조회에 실패했습니다."),
    GET_MY_POST_OF_CLIPBOOK_FAIL(false,4672,"스크랩북의 내 게시글 조회에 실패했습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
