package kr.adapterz.ari_community.domain.auth;

import jakarta.transaction.Transactional;
import kr.adapterz.ari_community.domain.auth.dto.request.LoginRequest;
import kr.adapterz.ari_community.domain.auth.dto.response.LoginResponse;
import kr.adapterz.ari_community.domain.auth.dto.response.TokenRefreshResponse;
import kr.adapterz.ari_community.domain.user.User;
import kr.adapterz.ari_community.domain.user.UserRepository;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import kr.adapterz.ari_community.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 사용자 로그인 처리 및 Access Token 생성
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }
        // Access Token 발급
        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getEmail());
        log.info("로그인 성공, Access Token 발급 - userId: {}", user.getUserId());
        return new LoginResponse(accessToken, user);
    }

    // Refresh Token을 사용하여 새로운 Access Token 발급
    public TokenRefreshResponse reissueAccessToken(String refreshToken) {
        // 위조/만료 여부 검증
        jwtUtil.validateRefreshToken(refreshToken);
        Integer userId = jwtUtil.getUserId(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 새로운 Access Token 발급
        String newAccessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getEmail());
        log.info("Access 토큰 재발급 - userId: {}", userId);
        return new TokenRefreshResponse(newAccessToken);
    }
}
