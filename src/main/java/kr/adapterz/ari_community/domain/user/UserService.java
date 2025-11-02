package kr.adapterz.ari_community.domain.user;

import jakarta.transaction.Transactional;
import kr.adapterz.ari_community.domain.user.dto.request.UpdatePasswordRequest;
import kr.adapterz.ari_community.domain.user.dto.request.UpdateUserRequest;
import kr.adapterz.ari_community.domain.user.dto.response.UpdateUserResponse;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import kr.adapterz.ari_community.global.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.password());
        user.updatePassword(encodedPassword);
        return new UpdateUserResponse(user);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

}

