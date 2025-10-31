package kr.adapterz.ari_community.domain.postLike;

import jakarta.persistence.Entity;
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
    @ManyToOne
    @JoinColumn(name = "postId")
    private BigInteger postId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Integer userId;

    public PostLike(BigInteger postId, Integer userId) {
        this.postId = postId;
        this.userId = userId;
    }

}

