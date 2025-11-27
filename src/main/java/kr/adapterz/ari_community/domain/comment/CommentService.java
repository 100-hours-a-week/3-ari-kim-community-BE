package kr.adapterz.ari_community.domain.comment;

import kr.adapterz.ari_community.domain.comment.dto.CreateOrUpdateCommentRequest;
import kr.adapterz.ari_community.domain.comment.dto.GetCommentsResponse;
import kr.adapterz.ari_community.domain.post.Post;
import kr.adapterz.ari_community.domain.post.PostRepository;
import kr.adapterz.ari_community.domain.user.User;
import kr.adapterz.ari_community.domain.user.UserRepository;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /* 댓글 조회
    특정 게시물의 댓글을 조회
    최초 조회시 comment_id 내림차순에서 1-10번째 댓글을 가져옴
    다음 페이지 조회시 comment_id 내림차순에서 cursorId(마지막으로 조회한 댓글) 이후 1-10번째 댓글을 가져옴
    가져온 댓글들을 DTO로 변환하여 반환
     */
    public Slice<GetCommentsResponse> getComments(BigInteger postId, BigInteger cursorId, Integer size) {
        Slice<Comment> commentSlice;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "commentId"));
        if (cursorId == null) {
            commentSlice = commentRepository.findByPost_PostIdOrderByCommentIdDesc(postId, pageable);
        } else {
            commentSlice = commentRepository.findByPost_PostIdAndCommentIdLessThanOrderByCommentIdDesc(postId, cursorId, pageable);
        }
        return commentSlice.map(comment -> new GetCommentsResponse(comment));
    }

    /* 댓글 등록
    postId와 RequestDTO로 user_id, 내용을 가져옴
    해당 Post와 User를 찾고 Comment를 생성해 DB에 저장함
    */
    @Transactional
    public GetCommentsResponse createComment(BigInteger postId, CreateOrUpdateCommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Comment comment = request.toEntity(post, user);
        Comment savedComment = commentRepository.save(comment);
        // 게시물의 댓글 수 증가
        post.increaseCommentCount();
        return new GetCommentsResponse(savedComment);
    }

    /* 댓글 수정
    postId, commentId와 RequestDTO로 user_id, 내용을 가져옴
    해당 Post와 User를 찾고 Comment를 생성해 DB에 저장함
    */
    @Transactional
    public GetCommentsResponse updateComment(Integer commentId, CreateOrUpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        comment.updateComment(request.content());
        return new GetCommentsResponse(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        Post post = comment.getPost();
        commentRepository.delete(comment);
        // 게시물의 댓글 수 감소
        if (post != null) {
            post.decreaseCommentCount();
        }
    }

}
