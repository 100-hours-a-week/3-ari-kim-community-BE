package kr.adapterz.ari_community.domain.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Slice<Comment> findAllByOrderByCommentIdDesc(Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.commentId < :cursorId ORDER BY c.commentId DESC")
    Slice<Comment> findByCommentIdLessThanOrderByCommentIdDesc(@Param("cursorId") BigInteger cursorId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId ORDER BY c.commentId DESC")
    Slice<Comment> findByPost_PostIdOrderByCommentIdDesc(@Param("postId") BigInteger postId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId AND c.commentId < :cursorId ORDER BY c.commentId DESC")
    Slice<Comment> findByPost_PostIdAndCommentIdLessThanOrderByCommentIdDesc(@Param("postId") BigInteger postId, @Param("cursorId") BigInteger cursorId, Pageable pageable);

    List<Comment> findByUser_UserId(Integer userId);

}
