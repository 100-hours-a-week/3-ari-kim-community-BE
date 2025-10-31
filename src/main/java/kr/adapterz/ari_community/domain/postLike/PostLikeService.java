package kr.adapterz.ari_community.domain.postLike;

import kr.adapterz.ari_community.domain.post.Post;
import kr.adapterz.ari_community.domain.post.PostRepository;
import kr.adapterz.ari_community.domain.user.UserRepository;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    // 게시물 좋아요 여부 조회 후 등록/삭제
    public Boolean getPostLike(BigInteger postId, Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        PostLike postLike = postLikeRepository.findByUserId(userId);

        // 게시물 좋아요를 누른 사용자이면 좋아요 삭제, Post에서 LikeCount -1
        if (postLike != null) {
            postLikeRepository.delete(postLike);
            post.setLikeCount(post.getLikeCount()-1);
            return false;
        }
        // 게시물 좋아요를 누르지 않은 사용자이면 좋아요 등록, Post에서 LikeCount +1
        else {
            new PostLike(postId, userId);
            post.setLikeCount(post.getLikeCount()+1);
            return true;
        }
    }

}
