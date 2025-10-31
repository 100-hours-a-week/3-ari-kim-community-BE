package kr.adapterz.ari_community.domain.postLike;

import kr.adapterz.ari_community.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    // 게시물 좋아요 여부 조회 후 등록/삭제
    public Boolean getPostLike(BigInteger postId, Integer userId) {
        Boolean isLiked = false;

        // 게시물 좋아요 리스트에 사용자가 있다면 좋아요 삭제(좋아요 리스트에서 사용자 삭제, 좋아요수 -1)
        if (postLikeRepository.findById(userId) != null) {
            isLiked = true;
            postLikeRepository.delete(userId);

        }

    }
}
