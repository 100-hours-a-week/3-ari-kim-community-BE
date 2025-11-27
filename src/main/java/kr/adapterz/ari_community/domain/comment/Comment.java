package kr.adapterz.ari_community.domain.comment;

import jakarta.persistence.*;
import kr.adapterz.ari_community.domain.post.Post;
import kr.adapterz.ari_community.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean isModified;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    public Comment(Post post, User user, String content) {
        // 추후 유효성 검사 추가
        if (content == null || content.isEmpty()) {
            throw new kr.adapterz.ari_community.global.exception.CustomException(
                kr.adapterz.ari_community.global.exception.ErrorCode.INVALID_INPUT_VALUE
            );
        }
        this.post = post;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.content = content;
        // 필수 필드 초기화
        this.isModified = false;
    }

    public void updateComment(String content) {
        this.content = content;
        this.isModified = true;
    }

}
