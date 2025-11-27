package kr.adapterz.ari_community.domain.auth.dto.response;

import kr.adapterz.ari_community.domain.user.User;

public record LoginResponse(

        String accessToken,

        Integer userId,

        String email,

        String nickname,

        String profileUrl

) {

        public LoginResponse(String accessToken, User user) {
                this(accessToken, 
                user.getUserId(), 
                user.getEmail(), 
                user.getNickname(),
                user.getProfileUrl());
        }

}
