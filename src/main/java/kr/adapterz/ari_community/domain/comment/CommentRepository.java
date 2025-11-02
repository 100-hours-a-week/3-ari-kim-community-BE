package kr.adapterz.ari_community.domain.comment;

import kr.adapterz.ari_community.domain.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Slice<Comment> findAllByOrderByCommentIdDesc(Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.commentId < :cursorId ORDER BY c.commentId DESC")
    Slice<Comment> findByCommentIdLessThanOrderByCommentIdDesc(@Param("cursorId") BigInteger cursorId, Pageable pageable);

}
