package kr.adapterz.ari_community.domain.post;

import jakarta.transaction.Transactional;
import kr.adapterz.ari_community.domain.comment.CommentRepository;
import kr.adapterz.ari_community.domain.post.dto.request.CreateOrUpdatePostRequest;
import kr.adapterz.ari_community.domain.post.dto.response.GetPostDetailResponse;
import kr.adapterz.ari_community.domain.post.dto.response.GetPostListResponse;
import kr.adapterz.ari_community.domain.postLike.PostLikeRepository;
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

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    /* 게시물 목록 조회
    최초 조회시 post_id 오름차순에서 1-20번째 게시물을 가져옴
    다음 페이지 조회시 post_id 오름차순에서 cursorId(마지막으로 조회한 게시물) 이후 1-20번째 게시물을 가져옴
    가져온 게시물들을 DTO로 변환하여 반환
    */
    public Slice<GetPostListResponse> getPostList(BigInteger cursorId, Integer size) {
        Slice<Post> postSlice;
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "postId"));

        if (cursorId == null) { // 최초 조회시
            postSlice = postRepository.findAllByOrderByPostIdDesc(pageable);
        } else {
            postSlice = postRepository.findByPostIdLessThanOrderByPostIdDesc(cursorId, pageable);
        }

        return postSlice.map(post -> new GetPostListResponse(post));
    }

    /* 게시물 상세 조회
    post_id에 해당하는 게시물을 가져옴
    조회 시 조회수 증가
    */
    @Transactional
    public GetPostDetailResponse getPost(BigInteger postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.increaseViewCount(); // 조회수 증가
        return new GetPostDetailResponse(post);
    }


    /* 게시물 등록
    RequestDTO로 user_id, 제목, 내용을 가져오고, imageFile을 받음
    user_id로 해당 User를 가져오고, imageFile을 서버에 저장하고 URL을 받아 DB에 저장함
    */
    @Transactional
    public GetPostDetailResponse createPost(CreateOrUpdatePostRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = request.toEntity(user, request.imageUrl());
        Post savedPost = postRepository.save(post);
        return new GetPostDetailResponse(savedPost);
    }

    /* 게시물 수정
    RequestDTO로 게시물 정보(제목, 내용, 이미지URL)를 가져오고, 이를 post_id에 해당하는 post에 적용함
    isModified=0, 이미지 URL=null이면 기존 이미지 적용
    */
    @Transactional
    public GetPostDetailResponse updatePost(BigInteger postId, CreateOrUpdatePostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        
        // imageUrl이 제공된 경우에만 업데이트, null이면 기존 값 유지
        String imageUrl = request.imageUrl() != null ? request.imageUrl() : post.getImageUrl();
        
        post.updatePost(request.title(), request.content(), imageUrl);
        return new GetPostDetailResponse(post);
    }

    // 게시물 삭제
    @Transactional
    public void deletePost(BigInteger postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        
        // 게시물 삭제 전에 관련된 댓글과 좋아요 먼저 삭제
        // 댓글 삭제 (Pageable 없이 모든 댓글 조회)
        List<kr.adapterz.ari_community.domain.comment.Comment> comments = 
            commentRepository.findByPost_PostIdOrderByCommentIdDesc(postId, Pageable.unpaged()).getContent();
        commentRepository.deleteAll(comments);
        
        // 좋아요 삭제
        postLikeRepository.deleteAll(postLikeRepository.findByPost_PostId(postId));
        
        // 게시물 삭제
        postRepository.delete(post);
    }

}
