package kr.adapterz.ari_community.domain.postLike;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.adapterz.ari_community.domain.post.Post;
import kr.adapterz.ari_community.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Getter
@NoArgsConstructor
public class PostLike {

    @Id
    @GeneratedValue
    private BigInteger postLikeId;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }

}
