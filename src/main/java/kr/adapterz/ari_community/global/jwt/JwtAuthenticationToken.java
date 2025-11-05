package kr.adapterz.ari_community.global.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken implements Authentication {

    private final Integer userId;
    private final String email;
    private boolean authenticated = true;

    // JWT 인증 토큰 생성자
    public JwtAuthenticationToken(Integer userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    // 사용자 권한 목록 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    // 인증 정보의 자격 증명 반환
    @Override
    public Object getCredentials() {
        return null;
    }

    // 인증 정보의 상세 정보 반환
    @Override
    public Object getDetails() {
        return null;
    }

    // 인증 주체(사용자 ID) 반환
    @Override
    public Object getPrincipal() {
        return userId;
    }

    // 인증 여부 확인
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    // 인증 상태 설정
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    // 사용자 이름(이메일) 반환
    @Override
    public String getName() {
        return email;
    }

    // 사용자 ID 반환
    public Integer getUserId() {
        return userId;
    }

    // 사용자 이메일 반환
    public String getEmail() {
        return email;
    }
}


