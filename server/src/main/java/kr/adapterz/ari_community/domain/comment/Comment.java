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
    private Integer comment_id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean is_modified;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

}
