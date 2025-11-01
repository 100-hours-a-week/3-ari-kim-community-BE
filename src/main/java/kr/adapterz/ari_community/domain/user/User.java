package kr.adapterz.ari_community.domain.user;

import jakarta.persistence.*;
import kr.adapterz.ari_community.domain.comment.Comment;
import kr.adapterz.ari_community.domain.post.Post;
import kr.adapterz.ari_community.domain.postLike.PostLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Integer userId;

    @Column(nullable = false, unique=true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique=true)
    private String nickname;

    @Column(nullable = false)
    private String profileUrl;

    // 단방향 연관관계, post 테이블에 FK(user_id) 생성
    @OneToMany
    @JoinColumn(name = "userId")
    private List<Post> posts = new ArrayList<>();

    // 단방향 연관관계, comment 테이블에 FK(user_id) 생성
    @OneToMany
    @JoinColumn(name = "userId")
    private List<Comment> comments = new ArrayList<>();

    // 단방향 연관관계, post_like 테이블에 FK(user_id) 생성
    @OneToMany
    @JoinColumn(name = "userId")
    private List<PostLike> postLike = new ArrayList<>();

    public void updateUser(String nickname, String profileUrl) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
