package kr.adapterz.ari_community.domain.auth;

import jakarta.servlet.http.HttpServletResponse;
import kr.adapterz.ari_community.domain.auth.dto.request.LoginRequest;
import kr.adapterz.ari_community.domain.auth.dto.response.LoginResponse;
import kr.adapterz.ari_community.domain.auth.dto.response.TokenRefreshResponse;
import kr.adapterz.ari_community.global.util.CookieUtil;
import kr.adapterz.ari_community.global.jwt.JwtUtil;
import kr.adapterz.ari_community.global.success.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    // 로그인 처리 및 Access Token 반환, Refresh Token을 쿠키에 저장
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(request);

        // Refresh Token을 HttpOnly 쿠키에 설정
        String refreshToken = jwtUtil.generateRefreshToken(
                loginResponse.userId(),
                loginResponse.email()
        );
        cookieUtil.setRefreshTokenCookie(refreshToken, response);

        return ResponseEntity.ok(ApiResponse.success(loginResponse, "로그인에 성공했습니다."));
    }

    // Refresh Token을 사용하여 새로운 Access Token 발급
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new kr.adapterz.ari_community.global.exception.CustomException(
                    kr.adapterz.ari_community.global.exception.ErrorCode.TOKEN_NOT_FOUND
            );
        }

        TokenRefreshResponse tokenResponse = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(ApiResponse.success(tokenResponse, "토큰이 갱신되었습니다."));
    }

    // 로그아웃 처리 및 Refresh Token 쿠키 삭제
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        cookieUtil.deleteRefreshTokenCookie(response);
        return ResponseEntity.ok(ApiResponse.success(null, "로그아웃되었습니다."));
    }

}
