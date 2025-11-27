package kr.adapterz.ari_community.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private final long refreshTokenExpiration;

    // Refresh Token 만료 시간 설정
    public CookieUtil(@Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // Refresh Token을 HttpOnly 쿠키로 설정
    public void setRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken); // 쿠키 생성
        refreshTokenCookie.setHttpOnly(true); // HttpOnly 쿠키로 설정
        refreshTokenCookie.setSecure(true); // Secure 쿠키로 설정
        refreshTokenCookie.setPath("/"); // 쿠키 경로 설정
        refreshTokenCookie.setMaxAge((int) (refreshTokenExpiration / 1000)); // 초 단위로 변환하여 쿠키 만료 시간 설정
        response.addCookie(refreshTokenCookie); // 쿠키 추가
    }

    // Refresh Token 쿠키 삭제
    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
    }
}

