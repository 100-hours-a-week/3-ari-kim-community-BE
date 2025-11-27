package kr.adapterz.ari_community.domain.postLike;

import kr.adapterz.ari_community.global.success.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    /* 게시물 좋아요 여부 조회 (토글하지 않음)
    PathVariable로 postId, userId를 가져옴
    */
    @GetMapping("{postId}/likes/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> checkPostLike(@PathVariable BigInteger postId, @PathVariable Integer userId) {
        Boolean isLiked = postLikeService.checkPostLike(postId, userId);
        return ResponseEntity.ok(ApiResponse.success(isLiked));
    }

    /* 게시물 좋아요 토글 (등록/삭제)
    PathVariable로 postId, userId를 가져옴
    */
    @PostMapping("{postId}/likes/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> togglePostLike(@PathVariable BigInteger postId, @PathVariable Integer userId) {
        Boolean isLiked = postLikeService.togglePostLike(postId, userId);
        return ResponseEntity.ok(ApiResponse.success(isLiked));
    }

}
