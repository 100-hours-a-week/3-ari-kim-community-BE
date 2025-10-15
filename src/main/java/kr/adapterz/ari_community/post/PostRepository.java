package kr.adapterz.ari_community.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface PostRepository extends JpaRepository<Post, BigInteger> {

    Slice<Post> findAllOrderByIdAsc(Pageable pageable);

    Slice<Post> findAllByIdGreaterThanOrderByIdAsc(BigInteger id, Pageable pageable);

}
