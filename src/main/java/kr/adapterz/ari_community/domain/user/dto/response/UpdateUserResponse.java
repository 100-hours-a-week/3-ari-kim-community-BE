package kr.adapterz.ari_community.domain.user.dto.response;

import kr.adapterz.ari_community.domain.user.User;

public record UpdateUserResponse (

    Integer userId,

    String nickname,

    String profileUrl

) {

    public UpdateUserResponse(User user) {
        this(user.getUserId(), 
        user.getNickname(), 
        user.getProfileUrl());
    }

}