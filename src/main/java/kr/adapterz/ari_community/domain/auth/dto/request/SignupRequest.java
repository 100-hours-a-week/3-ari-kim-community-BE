package kr.adapterz.ari_community.domain.auth.dto.request;

public record SignupRequest (

    String email,

    String password,

    String nickname,

    // 비밀번호 확인은 프론트에서 검증
    String profileUrl

) {}