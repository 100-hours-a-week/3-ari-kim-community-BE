package kr.adapterz.ari_community.domain.comment.dto;

import kr.adapterz.ari_community.domain.comment.Comment;

import java.time.LocalDateTime;

public record GetCommentsResponse(

    Integer commentId,

    String nickname,

    String profileUrl,

    Boolean isModified,

    LocalDateTime createAt,

    String content

) {

    public GetCommentsResponse(Comment comment) {
        this(comment.getCommentId(),
                comment.getUser().getNickname(),
                comment.getUser().getProfileUrl(),
                false,
                LocalDateTime.now(),
                comment.getContent());
    }

}