package kr.adapterz.ari_community.domain.user.dto.request;

public record UpdatePasswordRequest (

    String password,

    String passwordCheck

) {}
