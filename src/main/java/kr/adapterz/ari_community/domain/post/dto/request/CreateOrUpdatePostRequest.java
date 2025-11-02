package kr.adapterz.ari_community.domain.post.dto.request;

public record CreateOrUpdatePostRequest (

    Integer userId,

    String title,

    String content

) {}