package kr.adapterz.ari_community.domain.postLike;

import kr.adapterz.ari_community.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface PostLikeRepository extends JpaRepository<PostLike, BigInteger> {

    PostLike findByUserId(Integer userId);

}
