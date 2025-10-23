package kr.adapterz.ari_community.domain.post;

import kr.adapterz.ari_community.domain.post.dto.request.CreateOrUpdatePostRequest;
import kr.adapterz.ari_community.domain.post.dto.response.GetPostListResponse;
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
    Response DTO 요소: ID, 닉네임, 제목, 수정여부, 작성시각, 좋아요수, 조회수, 댓글수
     */
    @GetMapping
    public ResponseEntity<Slice<GetPostListResponse>> getPostList(
            @RequestParam(required = false) BigInteger cursorId,
            @RequestParam(defaultValue = "20") int size) {
        Slice<GetPostListResponse> postList = postService.getPostList(cursorId, size);
        return ResponseEntity.ok(postList);
    }

    // 게시물 상세 조회
    @GetMapping("/{post_id}")
    public ResponseEntity<Post> getPost(@PathVariable BigInteger post_id) {
        Post post = postService.getPost(post_id);
        return ResponseEntity.ok(post);
    }

    /* 게시물 등록
    PathVariable로 user_id를, RequestBody로 DTO 요소들을 가져옴
    Request DTO 요소: 제목, 내용, 이미지 URL(선택)
     */
    @PostMapping
    public ResponseEntity<Post> createPost(@PathVariable Integer user_id, @RequestBody CreateOrUpdatePostRequest request) {
        Post createdPost = postService.createPost(user_id, request);
        return ResponseEntity.ok(createdPost);
    }

    /* 게시물 수정
    PathVariable로 post_id를 가져옴
    Request DTO 요소: 제목, 내용, 이미지 URL(선택)
     */
    @PatchMapping("/{post_id}")
    public ResponseEntity<Post> updatePost(@PathVariable BigInteger post_id, @RequestBody CreateOrUpdatePostRequest request) {
        Post updatedPost = postService.updatePost(post_id, request);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시물 삭제
    @DeleteMapping("/{post_id}")
    public ResponseEntity<String> deletePost(@PathVariable BigInteger post_id) {
        postService.deletePost(post_id);
        return ResponseEntity.ok("204 삭제 완료");
    }

}
