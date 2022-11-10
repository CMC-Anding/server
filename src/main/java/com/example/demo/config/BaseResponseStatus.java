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

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    S3_UPLOAD_ERROR(false, 4002, "S3 업로드에 실패했습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    //[GET] /app/questions/:filter-id
    GET_QUESTION_ERROR(false,4501,"사용자가 작성하지않은 질문을 가져오는데에 실패하였습니다."),

    // 자서전 관련
    MODIFY_FAIL_AUTOGRAPHY_ETC(false, 4550, "자서전의 제목, 세부설명, 색상의 정보 수정에 실패하였습니다."),
    DELETE_FAIL_AUTOGRAPHY_POST(false, 4551, "기존 자서전의 게시글 구성 삭제에 실패하였습니다."),
    INSERT_FAIL_AUTOGRAPHY_POST(false, 4552, "자서전의 게시글 구성 생성에 실패하였습니다."),

    // 스크랩 관련
    CLIP_FAIL(false, 4600,"나의 글 혹은 익명의 글을 스크랩하는데 실패하였습니다."),
    CLIP_DUPLICATE(false,4601,"이미 스크랩 한 게시글입니다."),
    DELETE_CLIP_FAIL(false, 4602, "스크랩북 수정(구성 게시글 삭제)에 실패하였습니다."),

    // 게시글 관련
    POST_DAILY_POST_FAIL(false,4507,"일상게시글(사진 제외)을 등록하는데 실패하였습니다.");

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
