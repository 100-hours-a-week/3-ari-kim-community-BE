package kr.adapterz.ari_community.post;

import kr.adapterz.ari_community.post.dto.request.CreateOrUpdatePostRequest;
import kr.adapterz.ari_community.post.dto.response.GetPostListResponse;
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

    @GetMapping
    public ResponseEntity<Slice<GetPostListResponse>> getPostList(
            @RequestParam(required = false) BigInteger cursorId,
            @RequestParam(defaultValue = "20") int size) {
        Slice<GetPostListResponse> postList = postService.getPostList(cursorId, size);
        return ResponseEntity.ok(postList);
    }

    @GetMapping
    public ResponseEntity<Post> getPost(@PathVariable BigInteger post_id) {
        Post post = postService.getPost(post_id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@PathVariable Integer user_id, @RequestBody CreateOrUpdatePostRequest request) {
        Post createdPost = postService.createPost(user_id, request);
        return ResponseEntity.ok(createdPost);
    }

    @PatchMapping("/{post_id}")
    public ResponseEntity<Post> updatePost(@PathVariable BigInteger post_id, @RequestBody CreateOrUpdatePostRequest request) {
        Post updatedPost = postService.updatePost(post_id, request);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<String> deletePost(@PathVariable BigInteger post_id) {
        postService.deletePost(post_id);
        return ResponseEntity.ok("204 삭제 완료");
    }

}
