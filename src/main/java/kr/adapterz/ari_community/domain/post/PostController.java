package kr.adapterz.ari_community.domain.post;

import kr.adapterz.ari_community.domain.post.dto.request.CreateOrUpdatePostRequest;
import kr.adapterz.ari_community.domain.post.dto.response.GetPostDetailResponse;
import kr.adapterz.ari_community.domain.post.dto.response.GetPostListResponse;
import kr.adapterz.ari_community.global.success.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    /* 게시물 목록 조회 (무한 스크롤링)
    URL: /posts?size=0 (첫페이지) 혹은 /posts?cursorId=0&size=0
    Response DTO 요소: postId, 닉네임, 제목, 수정여부, 작성시각, 좋아요수, 조회수, 댓글수
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Slice<GetPostListResponse>>> getPostList(
            @RequestParam(required = false) BigInteger cursorId,
            @RequestParam(defaultValue = "20") int size) {
        Slice<GetPostListResponse> postList = postService.getPostList(cursorId, size);
        return ResponseEntity.ok(ApiResponse.success(postList));
    }

    // 게시물 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<GetPostDetailResponse>> getPost(@PathVariable BigInteger postId) {
        GetPostDetailResponse post = postService.getPost(postId);
        return ResponseEntity.ok(ApiResponse.success(post));
    }

    /* 게시물 등록
    RequestBody로 DTO 요소들을 가져옴
    Request DTO 요소: user_id, 제목, 내용, 이미지 URL(선택)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<GetPostDetailResponse>> createPost(
            @RequestBody CreateOrUpdatePostRequest request
            ) {
        GetPostDetailResponse createdPost = postService.createPost(request);
        return ResponseEntity.ok(ApiResponse.success(createdPost));
    }

    /* 게시물 수정
    PathVariable로 post_id를 가져옴
    Request DTO 요소: 제목, 내용, 이미지 URL(선택)
     */
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<GetPostDetailResponse>> updatePost(
            @PathVariable BigInteger postId,
            @RequestBody CreateOrUpdatePostRequest request
            ) {
        GetPostDetailResponse updatedPost = postService.updatePost(postId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedPost));
    }

    // 게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable BigInteger postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

}
