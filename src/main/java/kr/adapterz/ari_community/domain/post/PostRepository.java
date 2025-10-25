package kr.adapterz.ari_community.domain.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface PostRepository extends JpaRepository<Post, BigInteger> {

    Slice<Post> findAllByOrderByPostIdDesc(Pageable pageable);

    Slice<Post> findByPostIdLessThanOrderByPostIdDesc(BigInteger postId, Pageable pageable);

}
