package kr.adapterz.ari_community.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UpdatePasswordRequest {

    private String password;

    private String password_check;

}
