package kr.adapterz.ari_community.domain.user.dto.response;

import kr.adapterz.ari_community.domain.user.User;
import lombok.Getter;

@Getter
public class UpdateUserResponse {

    private final Integer user_id;
    private final String nickname;
    private final String password;
    private final String profile_url;

    public UpdateUserResponse(User user) {
        this.user_id = user.getUser_id();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
        this.profile_url = user.getProfile_url();
    }
}
