package kr.adapterz.ari_community.post.dto.response;

import kr.adapterz.ari_community.post.Post;
import lombok.Getter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
public class GetPostListResponse {

    private BigInteger post_id;

    private String nickname;

    private String title;

    private Boolean is_modified;

    private LocalDateTime created_at;

    private Integer like_count;

    private BigInteger view_count;

    private Integer comment_count;

    public GetPostListResponse(Post post) {
        this.post_id = post.getPost_id();
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.is_modified = post.getIs_modified();
        this.created_at = post.getCreated_at();
        this.like_count = post.getLike_count();
        this.view_count = post.getView_count();
        this.comment_count = post.getComment_count();
    }

}
