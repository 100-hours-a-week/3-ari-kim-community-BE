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
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        // 비밀번호 검증 (실제로는 BCrypt 등으로 암호화되어 있을 것)
        if (!Objects.equals(user.getPassword(), request.password())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getEmail());

        return new LoginResponse(
                accessToken,
                user.getUserId(),
                user.getEmail(),
                user.getNickname()
        );
    }

    public TokenRefreshResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String tokenType = jwtUtil.getTokenType(refreshToken);
        if (!"REFRESH".equals(tokenType)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Integer userId = jwtUtil.getUserId(refreshToken);
        String email = jwtUtil.getEmail(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getEmail());

        return new TokenRefreshResponse(newAccessToken);
    }
}
