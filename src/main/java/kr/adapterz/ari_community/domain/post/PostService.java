package kr.adapterz.ari_community.domain.post;

import jakarta.transaction.Transactional;
import kr.adapterz.ari_community.domain.post.dto.request.CreateOrUpdatePostRequest;
import kr.adapterz.ari_community.domain.post.dto.response.GetPostDetailResponse;
import kr.adapterz.ari_community.domain.post.dto.response.GetPostListResponse;
import kr.adapterz.ari_community.domain.user.User;
import kr.adapterz.ari_community.domain.user.UserRepository;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /* 게시물 목록 조회
    최초 조회시 post_id 오름차순에서 1-20번째 게시물을 가져옴
    다음 페이지 조회시 post_id 오름차순에서 cursorId(마지막으로 조회한 게시물) 이후 1-20번째 게시물을 가져옴
    가져온 게시물들을 DTO로 변환하여 반환
     */
    public Slice<GetPostListResponse> getPostList(BigInteger cursorId, Integer size) {
        Slice<Post> postSlice;
        Pageable pageable = PageRequest.of(0, size);

        if (cursorId == null) { // 최초 조회시
            postSlice = postRepository.findAllOrderByIdAsc(pageable);
        } else {
            postSlice = postRepository.findAllByIdGreaterThanOrderByIdAsc(cursorId, pageable);
        }

        return postSlice.map(post -> new GetPostListResponse(post));
    }

    /* 게시물 상세 조회
    post_id에 해당하는 게시물을 가져옴
     */
    public GetPostDetailResponse getPost(BigInteger post_id) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return new GetPostDetailResponse(post);
    }

    /* 게시물 등록
    user_id로 User정보(user, nickname)을, RequestDTO로 게시물 정보(제목, 내용, 이미지URL)를 가져와 DB에 저장함
     */
    @Transactional
    public Post createPost(Integer user_id, CreateOrUpdatePostRequest request) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = new Post(user,
                user.getNickname(),
                request.getTitle(),
                request.getContent(),
                request.getImage_url());
        return postRepository.save(post);
    }

    /* 게시물 수정
    RequestDTO로 게시물 정보(제목, 내용, 이미지URL)를 가져오고, 이를 post_id에 해당하는 post에 적용함
     */
    @Transactional
    public Post updatePost(BigInteger post_id, CreateOrUpdatePostRequest request) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setImage_url(request.getImage_url());
        return post;
    }

    // 게시물 삭제
    @Transactional
    public void deletePost(BigInteger post_id) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        postRepository.delete(post);
    }

}
