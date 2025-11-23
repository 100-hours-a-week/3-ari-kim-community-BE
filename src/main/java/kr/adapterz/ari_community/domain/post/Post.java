package kr.adapterz.ari_community.domain.post;

import jakarta.persistence.*;
import kr.adapterz.ari_community.domain.comment.Comment;
import kr.adapterz.ari_community.domain.user.User;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    private BigInteger postId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false)
    private String title;

    // 수정 여부: 기본값 0, 게시물 수정시 0->1
    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isModified;

    // 작성 시각: yyyy-mm-dd hh:mm:ss
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String imageUrl;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer likeCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private BigInteger viewCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer commentCount;

    // 단방향 연관관계, comment 테이블에 FK(post_id) 생성
    @OneToMany
    @JoinColumn(name = "postId")
    private List<Comment> comments = new ArrayList<>();

    public Post(User user, String title, String content, String imageUrl) {
        String nickname = user.getNickname();

        if (nickname == null || nickname.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        } // 중복 검사 추가 필요
        if (title == null || title.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        if (content == null || content.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        this.user = user;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = LocalDateTime.now();
    }

    public void updatePost(String title, String content, String imageUrl) {
        if (title == null || title.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        if (content == null || content.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        this.title = title;
        this.isModified = true;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public void increasePostLike() {
        this.likeCount++;
    }

    public void decreasePostLike() {
        this.likeCount--;
    }

    public void increaseViewCount() {
        if (this.viewCount == null) {
            this.viewCount = BigInteger.ZERO;
        }
        this.viewCount = this.viewCount.add(BigInteger.ONE);
    }

}
