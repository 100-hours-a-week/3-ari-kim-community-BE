package kr.adapterz.ari_community.domain.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, BigInteger> {

    Slice<Post> findAllByOrderByPostIdDesc(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postId < :cursorId ORDER BY p.postId DESC")
    Slice<Post> findByPostIdLessThanOrderByPostIdDesc(@Param("cursorId") BigInteger cursorId, Pageable pageable);

    List<Post> findByUser_UserId(Integer userId);

}
