package kr.adapterz.ari_community.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    // 컨트롤러 실행 전 세션 확인
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        // 세션이 없거나 userId가 없으면 인증 실패
        if (session == null || session.getAttribute("userId") == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return true;
    }

}
