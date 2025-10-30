package kr.adapterz.ari_community.domain.post.dto.response;

import kr.adapterz.ari_community.domain.post.Post;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class GetPostDetailResponse {

    private BigInteger postId;

    private Integer userId;

    private String nickname;

    private String title;

    private Boolean isModified;

    private LocalDateTime createdAt;

    private String content;

    private String imageUrl;

    private Integer likeCount;

    private BigInteger viewCount;

    private Integer commentCount;

    public GetPostDetailResponse(Post post) {
        this.postId = post.getPostId();
        this.userId = post.getUser().getUserId();
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.isModified = post.getIsModified();
        this.createdAt = post.getCreatedAt();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.commentCount = post.getCommentCount();
    }

}
