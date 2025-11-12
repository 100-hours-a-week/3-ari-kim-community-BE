package kr.adapterz.ari_community.domain.user.dto.request;

public record SignupRequest(

        String email,

        String password,

        String nickname,

        String profileUrl
        
) {}
