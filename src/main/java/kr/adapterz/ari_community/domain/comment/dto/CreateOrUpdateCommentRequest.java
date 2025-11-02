package kr.adapterz.ari_community.domain.comment.dto;

public record CreateOrUpdateCommentRequest(

    Integer userId,

    String content

) {}