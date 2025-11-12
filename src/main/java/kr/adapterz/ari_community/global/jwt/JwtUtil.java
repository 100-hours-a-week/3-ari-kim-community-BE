package kr.adapterz.ari_community.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // Access Token 생성
    public String generateAccessToken(Integer userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("type", "ACCESS")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(Integer userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("type", "REFRESH")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    // 토큰에서 Claims 추출
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰에서 UserId 추출
    public Integer getUserId(String token) {
        Claims claims = extractClaims(token);
        return Integer.parseInt(claims.getSubject());
    }

    // 토큰에서 Email 추출
    public String getEmail(String token) {
        Claims claims = extractClaims(token);
        return claims.get("email", String.class);
    }

    // 토큰 타입 확인
    public String getTokenType(String token) {
        Claims claims = extractClaims(token);
        return claims.get("type", String.class);
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰을 검증하고 인증 객체를 반환 
    public JwtAuthenticationToken validateAndExtractAuthentication(String token) {
        try {
            Claims claims = extractClaims(token);
            // 만료 여부 확인
            if (claims.getExpiration().before(new Date())) {
                throw new CustomException(ErrorCode.EXPIRED_TOKEN);
            }
            // 토큰 타입 확인
            String tokenType = claims.get("type", String.class);
            if (!"ACCESS".equals(tokenType)) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
            // 인증 객체 생성 및 반환
            Integer userId = Integer.parseInt(claims.getSubject());
            String email = claims.get("email", String.class);

            return new JwtAuthenticationToken(userId, email);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    // Refresh Token 검증
    public void validateRefreshToken(String refreshToken) {
        if (!validateToken(refreshToken)) { // 토큰 유효성 검증
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String tokenType = getTokenType(refreshToken);
        if (!"REFRESH".equals(tokenType)) { // Refresh Token 타입 확인
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

}
