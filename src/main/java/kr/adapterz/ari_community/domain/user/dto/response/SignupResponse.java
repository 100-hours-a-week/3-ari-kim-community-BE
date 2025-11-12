package kr.adapterz.ari_community.domain.user.dto.response;

import kr.adapterz.ari_community.domain.user.User;

public record SignupResponse(

        Integer userId,

        String email,

        String nickname,

        String profileUrl

) {

    public SignupResponse(User user) {
        this(user.getUserId(), 
            user.getEmail(),
            user.getNickname(), 
            user.getProfileUrl());
    }

}
