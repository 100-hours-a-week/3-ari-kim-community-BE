package kr.adapterz.ari_community.domain.postLike;

import kr.adapterz.ari_community.domain.post.Post;
import kr.adapterz.ari_community.domain.post.PostRepository;
import kr.adapterz.ari_community.domain.user.User;
import kr.adapterz.ari_community.domain.user.UserRepository;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    // 게시물 좋아요 여부 조회 후 등록/삭제
    public Boolean getPostLike(BigInteger postId, Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        return postLikeRepository.findByPostIdAndUserId(postId, userId)
            // 게시물 좋아요를 누른 사용자이면 좋아요 삭제, Post에서 LikeCount -1
            .map(postLike -> {
                postLikeRepository.delete(postLike);
                post.decreasePostLike();
                postRepository.save(post);                    
                return false;
            })
            // 게시물 좋아요를 누르지 않은 사용자이면 좋아요 등록, Post에서 LikeCount +1
            .orElseGet(() -> {
                PostLike newPostLike = new PostLike(post, user);
                postLikeRepository.save(newPostLike);
                post.increasePostLike();
                postRepository.save(post);
                return true;
            });
    }

}
