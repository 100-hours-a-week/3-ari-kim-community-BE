package kr.adapterz.ari_community.domain.post.dto.response;

import kr.adapterz.ari_community.domain.post.Post;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
public class GetPostListResponse {

    private BigInteger postId;

    private String nickname;

    private String title;

    private Boolean isModified;

    private LocalDateTime createdAt;

    private Integer likeCount;

    private BigInteger viewCount;

    private Integer commentCount;

    public GetPostListResponse(Post post) {
        this.postId = post.getPostId();
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.isModified = post.getIsModified();
        this.createdAt = post.getCreatedAt();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.commentCount = post.getCommentCount();
    }

}
