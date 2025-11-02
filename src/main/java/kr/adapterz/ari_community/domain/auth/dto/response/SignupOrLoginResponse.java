package kr.adapterz.ari_community.domain.auth.dto.response;

import kr.adapterz.ari_community.domain.user.User;

public record SignupOrLoginResponse (

    Integer userId,

    String email,

    String nickname,

    String profileUrl

) {

    public SignupOrLoginResponse(User user) {
        this(user.getUserId(), 
            user.getEmail(),
            user.getNickname(), 
            user.getProfileUrl());
    }

}