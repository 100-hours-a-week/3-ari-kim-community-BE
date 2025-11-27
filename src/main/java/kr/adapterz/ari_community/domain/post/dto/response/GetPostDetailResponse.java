package kr.adapterz.ari_community.domain.post.dto.response;

import java.math.BigInteger;
import java.time.LocalDateTime;

import kr.adapterz.ari_community.domain.post.Post;

public record GetPostDetailResponse (

    BigInteger postId,

    Integer userId,

    String nickname,

    String profileUrl,

    String title,

    Boolean isModified,

    LocalDateTime createdAt,

    String content,

    String imageUrl,

    Integer likeCount,

    BigInteger viewCount,

    Integer commentCount

) {

    public GetPostDetailResponse(Post post) {
        this(post.getPostId(), 
        (post.getUser() != null) ? post.getUser().getUserId() : null, 
        (post.getUser() != null) ? post.getUser().getNickname() : "(탈퇴한 사용자)",
        (post.getUser() != null) ? post.getUser().getProfileUrl() : null,
        post.getTitle(), 
        post.getIsModified(), 
        post.getCreatedAt(), 
        post.getContent(), 
        post.getImageUrl(), 
        post.getLikeCount(), 
        post.getViewCount(), 
        post.getCommentCount());
    }

}