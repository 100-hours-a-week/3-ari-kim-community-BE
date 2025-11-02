package kr.adapterz.ari_community.domain.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;
import kr.adapterz.ari_community.domain.auth.dto.request.LoginRequest;
import kr.adapterz.ari_community.domain.auth.dto.request.SignupRequest;
import kr.adapterz.ari_community.domain.auth.dto.response.SignupOrLoginResponse;
import kr.adapterz.ari_community.domain.user.User;
import kr.adapterz.ari_community.global.success.ApiResponse;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /* 회원가입
    SignupRequest(이메일, 비밀번호, 닉네임, 프로필URL)를 가져옴 
    */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupOrLoginResponse>> signup(@RequestBody SignupRequest request) {
        SignupOrLoginResponse response = authService.signup(request);
        return ResponseEntity.ok(ApiResponse.success(response, "회원가입이 완료되었습니다."));
    }

    /* 로그인
    LoginRequest(이메일, 비밀번호)를 가져옴
    */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<SignupOrLoginResponse>> login(@RequestBody LoginRequest request, HttpSession session) {
        SignupOrLoginResponse response = authService.login(request, session);
        return ResponseEntity.ok(ApiResponse.success(response, "로그인에 성공했습니다."));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok(ApiResponse.success(null, "로그아웃되었습니다."));
    }

    // 현재 로그인한 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<SignupOrLoginResponse>> getCurrentUser(HttpSession session) {
        User user = authService.getCurrentUser(session);
        SignupOrLoginResponse response = new SignupOrLoginResponse(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

