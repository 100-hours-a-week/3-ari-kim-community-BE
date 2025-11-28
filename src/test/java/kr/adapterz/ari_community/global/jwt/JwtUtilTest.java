package kr.adapterz.ari_community.global.jwt;

import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JwtUtil 단위 테스트")
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String TEST_SECRET = "8d1f7c02b6e8f4a9a0c1d1e6f7b3a9e2d3f5b7c8a1e4d0b6c9f2a7d1e8c0f3b5";
    private static final long ACCESS_TOKEN_EXPIRATION = 3600000L; // 1시간
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000L; // 7일
    private static final Integer TEST_USER_ID = 1;
    private static final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(
                TEST_SECRET,
                ACCESS_TOKEN_EXPIRATION,
                REFRESH_TOKEN_EXPIRATION
        );
    }

    @Test
    @DisplayName("Access Token 생성 및 검증 성공")
    void testGenerateAndValidateAccessToken() {
        String accessToken = jwtUtil.generateAccessToken(TEST_USER_ID, TEST_EMAIL);

        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());
        assertTrue(jwtUtil.validateToken(accessToken));
        assertFalse(jwtUtil.isTokenExpired(accessToken));
    }

    @Test
    @DisplayName("Refresh Token 생성 및 검증 성공")
    void testGenerateAndValidateRefreshToken() {
        String refreshToken = jwtUtil.generateRefreshToken(TEST_USER_ID, TEST_EMAIL);

        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        assertTrue(jwtUtil.validateToken(refreshToken));
        assertFalse(jwtUtil.isTokenExpired(refreshToken));
    }

    @Test
    @DisplayName("토큰에서 userId 추출 성공")
    void testGetUserIdFromToken() {
        String accessToken = jwtUtil.generateAccessToken(TEST_USER_ID, TEST_EMAIL);

        Integer extractedUserId = jwtUtil.getUserId(accessToken);

        assertEquals(TEST_USER_ID, extractedUserId);
    }

    @Test
    @DisplayName("토큰에서 email 추출 성공")
    void testGetEmailFromToken() {
        String accessToken = jwtUtil.generateAccessToken(TEST_USER_ID, TEST_EMAIL);

        String extractedEmail = jwtUtil.getEmail(accessToken);

        assertEquals(TEST_EMAIL, extractedEmail);
    }

    @Test
    @DisplayName("Access Token 타입 확인 성공")
    void testGetAccessTokenType() {
        String accessToken = jwtUtil.generateAccessToken(TEST_USER_ID, TEST_EMAIL);

        String tokenType = jwtUtil.getTokenType(accessToken);

        assertEquals("ACCESS", tokenType);
    }

    @Test
    @DisplayName("Refresh Token 타입 확인 성공")
    void testGetRefreshTokenType() {
        String refreshToken = jwtUtil.generateRefreshToken(TEST_USER_ID, TEST_EMAIL);

        String tokenType = jwtUtil.getTokenType(refreshToken);

        assertEquals("REFRESH", tokenType);
    }

    @Test
    @DisplayName("만료된 토큰 검증 실패")
    void testExpiredTokenValidation() {
        // 만료 시간이 0인 JwtUtil 생성
        JwtUtil expiredJwtUtil = new JwtUtil(
                TEST_SECRET,
                0L, // 즉시 만료
                0L
        );
        String expiredToken = expiredJwtUtil.generateAccessToken(TEST_USER_ID, TEST_EMAIL);

        // 잠시 대기하여 토큰이 확실히 만료되도록 함
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(expiredJwtUtil.isTokenExpired(expiredToken));
        assertFalse(expiredJwtUtil.validateToken(expiredToken));
    }

    @Test
    @DisplayName("ExtractAuthentication - 유효한 Access Token으로 인증 객체 생성 성공")
    void testExtractAuthenticationWithValidAccessToken() {
        String accessToken = jwtUtil.generateAccessToken(TEST_USER_ID, TEST_EMAIL);

        JwtAuthenticationToken authentication = jwtUtil.ExtractAuthentication(accessToken);

        assertNotNull(authentication);
        assertEquals(TEST_USER_ID, authentication.getUserId());
        assertEquals(TEST_EMAIL, authentication.getEmail());
    }

    @Test
    @DisplayName("ExtractAuthentication - 만료된 토큰 사용 시 예외 발생")
    void testExtractAuthenticationWithExpiredTokenThrowsException() {
        // 만료 시간이 0인 JwtUtil 생성
        JwtUtil expiredJwtUtil = new JwtUtil(
                TEST_SECRET,
                0L, // 즉시 만료
                0L
        );
        String expiredToken = expiredJwtUtil.generateAccessToken(TEST_USER_ID, TEST_EMAIL);

        // 잠시 대기하여 토큰이 확실히 만료되도록 함
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        CustomException exception = assertThrows(CustomException.class, () -> {
            expiredJwtUtil.ExtractAuthentication(expiredToken);
        });
        assertEquals(ErrorCode.EXPIRED_TOKEN, exception.getErrorCode());
    }

    @Test
    @DisplayName("validateRefreshToken - 유효한 Refresh Token 검증 성공")
    void testValidateRefreshTokenWithValidToken() {
        String refreshToken = jwtUtil.generateRefreshToken(TEST_USER_ID, TEST_EMAIL);

        assertDoesNotThrow(() -> jwtUtil.validateRefreshToken(refreshToken));
    }

    @Test
    @DisplayName("다른 사용자의 토큰 생성 및 검증")
    void testTokenGenerationForDifferentUsers() {
        // given
        Integer userId1 = 1;
        String email1 = "user1@example.com";
        Integer userId2 = 2;
        String email2 = "user2@example.com";

        // when
        String token1 = jwtUtil.generateAccessToken(userId1, email1);
        String token2 = jwtUtil.generateAccessToken(userId2, email2);

        // then
        assertEquals(userId1, jwtUtil.getUserId(token1));
        assertEquals(email1, jwtUtil.getEmail(token1));
        assertEquals(userId2, jwtUtil.getUserId(token2));
        assertEquals(email2, jwtUtil.getEmail(token2));
        assertNotEquals(token1, token2);
    }

}