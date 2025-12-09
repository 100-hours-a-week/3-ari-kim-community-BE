package kr.adapterz.ari_community.domain.user;

import jakarta.transaction.Transactional;
import kr.adapterz.ari_community.domain.comment.CommentRepository;
import kr.adapterz.ari_community.domain.post.PostRepository;
import kr.adapterz.ari_community.domain.postLike.PostLikeRepository;
import kr.adapterz.ari_community.domain.user.dto.request.SignupRequest;
import kr.adapterz.ari_community.domain.user.dto.request.UpdatePasswordRequest;
import kr.adapterz.ari_community.domain.user.dto.request.UpdateUserRequest;
import kr.adapterz.ari_community.domain.user.dto.response.SignupResponse;
import kr.adapterz.ari_community.domain.user.dto.response.UpdateUserResponse;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    /* 회원가입
    RequestDTO로 회원 정보(이메일, 비밀번호, 닉네임)를 가져오고, imageFile을 받음
    중복 확인, 암호화 과정을 거친 뒤 사용자 생성 및 저장함
    */
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        // 이메일 중복 확인
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATION);
        }
        // 닉네임 중복 확인
        if (userRepository.findByNickname(request.nickname()).isPresent()) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATION);
        }
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());

        // 사용자 생성 및 저장 (profileUrl은 클라이언트가 S3에 업로드한 URL)
        User user = new User(
                request.email(),
                encodedPassword,
                request.nickname(),
                request.profileUrl()
        );
        User savedUser = userRepository.save(user);

        return new SignupResponse(savedUser);
    }

    /* 회원 정보 수정
    RequestDTO로 회원 정보(닉네임, 프로필URL)를 가져오고, 이를 user_id에 해당하는 user에 적용함
     */
    @Transactional
    public UpdateUserResponse updateUser(Integer userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NICKNAME_DUPLICATION));
        user.updateUser(request.nickname(), request.profileUrl());
        return new UpdateUserResponse(user);
    }

    // 비밀번호 변경
    @Transactional
    public UpdateUserResponse updatePassword(Integer userId, UpdatePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 비밀번호와 비밀번호 확인이 일치하는지 검증
        if (!request.password().equals(request.passwordCheck())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
        
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());
        user.updatePassword(encodedPassword);
        
        // 변경사항 저장 (JPA는 자동으로 저장하지만 명시적으로 저장)
        userRepository.save(user);
        
        return new UpdateUserResponse(user);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        // 사용자가 작성한 게시물 조회
        List<kr.adapterz.ari_community.domain.post.Post> userPosts = postRepository.findByUser_UserId(userId);
        
        // 각 게시물 삭제 (게시물 삭제 시 관련 댓글과 좋아요도 함께 삭제됨)
        for (kr.adapterz.ari_community.domain.post.Post post : userPosts) {
            BigInteger postId = post.getPostId();
            // 게시물의 댓글 삭제
            List<kr.adapterz.ari_community.domain.comment.Comment> comments = 
                commentRepository.findByPost_PostIdOrderByCommentIdAsc(postId, Pageable.unpaged()).getContent();
            commentRepository.deleteAll(comments);
            
            // 게시물의 좋아요 삭제
            postLikeRepository.deleteAll(postLikeRepository.findByPost_PostId(postId));
            
            // 게시물 삭제
            postRepository.delete(post);
        }
        
        // 사용자가 작성한 댓글 삭제 (다른 사용자의 게시물에 작성한 댓글)
        List<kr.adapterz.ari_community.domain.comment.Comment> userComments = 
            commentRepository.findByUser_UserId(userId);
        commentRepository.deleteAll(userComments);
        
        // 사용자가 누른 좋아요 삭제
        List<kr.adapterz.ari_community.domain.postLike.PostLike> userPostLikes = 
            postLikeRepository.findByUser_UserId(userId);
        postLikeRepository.deleteAll(userPostLikes);
        
        // 사용자 삭제
        userRepository.delete(user);
    }

}

