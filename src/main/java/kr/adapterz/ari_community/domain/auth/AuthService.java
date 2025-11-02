package kr.adapterz.ari_community.domain.auth;

import jakarta.servlet.http.HttpSession;
import kr.adapterz.ari_community.domain.auth.dto.request.LoginRequest;
import kr.adapterz.ari_community.domain.auth.dto.request.SignupRequest;
import kr.adapterz.ari_community.domain.auth.dto.response.SignupOrLoginResponse;
import kr.adapterz.ari_community.domain.user.User;
import kr.adapterz.ari_community.domain.user.UserRepository;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import kr.adapterz.ari_community.global.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* 회원가입
    SignupRequest(이메일, 비밀번호, 닉네임, 프로필URL)를 가져오고, 
    유효성 검사 후 새로운 User 객체 생성
    */
    @Transactional
    public SignupOrLoginResponse signup(SignupRequest request) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATION);
        }
        // 닉네임 중복 확인
        if (userRepository.existsByNickname(request.nickname())) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATION);
        }
        // BCrypt로 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());
        User newUser = new User(
            request.email(),
            encodedPassword, // 암호화된 비밀번호 저장
            request.nickname(),
            request.profileUrl()
        );
        userRepository.save(newUser);
        return new SignupOrLoginResponse(newUser);
    }

    /* 로그인
    LoginRequest(이메일, 비밀번호)를 가져옴
    이메일로 사용자를 찾은 후, 비밀번호 확인
    세션에 사용자 정보(userId, email) 저장하고, 유효 시간 설정
    */
    @Transactional(readOnly = true)
    public SignupOrLoginResponse login(LoginRequest request, HttpSession session) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));
        // 비밀번호 확인
        if (!passwordEncoder.checkPassword(request.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }
        // 세션에 사용자 정보(userId, email) 저장, 유효 시간 설정
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("email", user.getEmail());
        session.setMaxInactiveInterval(60 * 30);
        
        return new SignupOrLoginResponse(user);
    }

    /* 로그아웃
    세션에 저장된 정보를 삭제해 세션 무효화
    */
    public void logout(HttpSession session) {
        session.invalidate();
    }

    /* 현재 로그인한 사용자 조회
    세션에서 userId를 가져와 사용자 반환
     */
    @Transactional(readOnly = true)
    public User getCurrentUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    /* 로그인 여부 확인
    세션에 userId가 있는지 확인
    */
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }

}
