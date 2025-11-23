package kr.adapterz.ari_community.domain.comment;

import kr.adapterz.ari_community.domain.comment.dto.CreateOrUpdateCommentRequest;
import kr.adapterz.ari_community.domain.comment.dto.GetCommentsResponse;
import kr.adapterz.ari_community.global.success.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    /* 댓글 조회 (무한 스크롤링)
    URL: /posts/{postId}/comments?size=0 (첫페이지) 혹은 /posts/{postId}/comments?cursorId=0&size=0
    Response DTO 요소: commentId, 닉네임, 프로필URL, 수정여부, 작성시각, 내용
    */
    @GetMapping
    public ResponseEntity<ApiResponse<Slice<GetCommentsResponse>>> getComments(
            @RequestParam(required = false) BigInteger cursorId,
            @RequestParam(defaultValue = "10") int size) {
        Slice<GetCommentsResponse> comments = commentService.getComments(cursorId, size);
        return ResponseEntity.ok(ApiResponse.success(comments));
    }

    /* 댓글 등록
    RequestBody로 DTO 요소들을 가져옴
    Request DTO 요소: userId, 내용
    */
    @PostMapping
    public ResponseEntity<ApiResponse<GetCommentsResponse>> createPost(@RequestParam BigInteger postId, @RequestBody CreateOrUpdateCommentRequest request) {
        GetCommentsResponse createdComment = commentService.createComment(postId, request);
        return ResponseEntity.ok(ApiResponse.success(createdComment));
    }

    /* 댓글 수정
    RequestBody로 DTO 요소들을 가져옴
    Request DTO 요소: userId, 내용
    */
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<GetCommentsResponse>> UpdateComment(
            @RequestParam Integer commentId,
            @RequestBody CreateOrUpdateCommentRequest request) {
        GetCommentsResponse updatedComment = commentService.updateComment(commentId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedComment));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}