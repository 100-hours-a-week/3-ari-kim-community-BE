package kr.adapterz.ari_community.domain.postLike;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, BigInteger> {

    Optional<PostLike> findByPostIdAndUserId(BigInteger postId, Integer userId);

}
