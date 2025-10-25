package kr.adapterz.ari_community.domain.user.dto.response;

import kr.adapterz.ari_community.domain.user.User;
import lombok.Getter;

@Getter
public class UpdateUserResponse {

    private final Integer user_id;
    private final String nickname;
    private final String password;
    private final String profileUrl;

    public UpdateUserResponse(User user) {
        this.user_id = user.getUserId();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
        this.profileUrl = user.getProfileUrl();
    }
}
