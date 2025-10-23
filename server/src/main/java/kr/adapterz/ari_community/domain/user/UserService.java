package kr.adapterz.ari_community.domain.user;

import jakarta.transaction.Transactional;
import kr.adapterz.ari_community.domain.user.dto.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* 회원 정보 수정
    RequestDTO로 회원 정보(닉네임, 프로필URL)를 가져오고, 이를 user_id에 해당하는 user에 적용함
     */
    @Transactional
    public User updateUser(Integer user_id, UpdateUserRequest dto) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() ->
                        new IllegalArgumentException("user not found"));
        user.setNickname(dto.getNickname());
        user.setProfile_url(dto.getProfile_url());
        return user;
    }

    // 비밀번호 변경
    @Transactional
    public User updatePassword(Integer user_id, String password) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() ->
                        new IllegalArgumentException("user not found"));
        user.setPassword(password);
        return user;
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Integer user_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() ->
                        new IllegalArgumentException("user not found"));
        userRepository.delete(user);
    }

}

