package kr.adapterz.ari_community.domain.auth.dto.response;

public record LoginResponse(

        String accessToken,

        Integer userId,

        String email,

        String nickname

) {}
