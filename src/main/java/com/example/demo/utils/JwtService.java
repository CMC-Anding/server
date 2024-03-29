package com.example.demo.utils;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.config.secret.Secret;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class JwtService {

    /*
    JWT 생성
    @param userIdx
    @return String
     */
    public String createJwt(int userIdx) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userIdx", userIdx)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + Duration.ofHours(24 * 365).toMillis()))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_ACCESS_SECRET_KEY)
                .compact();
    }

    /*
    Header에서 X-ACCESS-TOKEN 으로 JWT 추출
    @return String
     */
    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /*
    JWT에서 userIdx 추출
    @return int
    @throws BaseException
     */
    public int getUserIdx() throws BaseException {
        //1. JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_ACCESS_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. userIdx 추출
        return claims.getBody().get("userIdx", Integer.class);
    }

    /*
    Refresh Token 생성
    @param userIdx
    @return String
     */
    public String createRefreshJwt(int userIdx) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofDays(365).toMillis());
        return Jwts.builder()
                .claim("userIdx", userIdx)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_REFRESH_SECRET_KEY)
                .compact();
    }

    /*
    토큰의 유효기간 검증
    @param token, isAccessToken
    @return boolean
     */
    public boolean  validateAccessTokenExpiration(String  token, boolean isAccessToken) throws BaseException{
        try{
            // 토큰 파싱
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(isAccessToken? Secret.JWT_ACCESS_SECRET_KEY:Secret.JWT_REFRESH_SECRET_KEY)
                    .parseClaimsJws(token);

            return claims.getBody().getExpiration().before(new Date()); // 현재보다 만료가 이전인지 확인
        }
        catch (ExpiredJwtException ignored){
            return  true;
        }catch (Exception e){
            throw   new BaseException(BaseResponseStatus.INVALID_JWT); // 만약 올바르지 않은 토큰이라면 에러
        }
    }

}
