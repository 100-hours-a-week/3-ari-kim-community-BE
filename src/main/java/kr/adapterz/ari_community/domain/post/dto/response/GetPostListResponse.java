package kr.adapterz.ari_community.domain.post.dto.response;

import java.math.BigInteger;
import java.time.LocalDateTime;

import kr.adapterz.ari_community.domain.post.Post;

public record GetPostListResponse (

    BigInteger postId,

    String nickname,

    String title,

    Boolean isModified,

    LocalDateTime createdAt,

    Integer likeCount,

    BigInteger viewCount,

    Integer commentCount

) {

    public GetPostListResponse(Post post) {
        this(post.getPostId(), 
        post.getNickname(), 
        post.getTitle(), 
        post.getIsModified(), 
        post.getCreatedAt(), 
        post.getLikeCount(), 
        post.getViewCount(), 
        post.getCommentCount());
    }

}
