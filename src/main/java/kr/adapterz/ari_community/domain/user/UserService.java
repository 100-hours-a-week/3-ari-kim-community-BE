package kr.adapterz.ari_community.domain.user;

import jakarta.transaction.Transactional;
import kr.adapterz.ari_community.domain.user.dto.request.SignupRequest;
import kr.adapterz.ari_community.domain.user.dto.request.UpdatePasswordRequest;
import kr.adapterz.ari_community.domain.user.dto.request.UpdateUserRequest;
import kr.adapterz.ari_community.domain.user.dto.response.SignupResponse;
import kr.adapterz.ari_community.domain.user.dto.response.UpdateUserResponse;
import kr.adapterz.ari_community.global.exception.CustomException;
import kr.adapterz.ari_community.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 이미지 파일을 서버에 저장
    private String saveImageToServer(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        // users 폴더에 "UUID_파일명.png" 으로 저장
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path imagePath = Paths.get("/Documents/images/users");

        try {
            if (!Files.exists(imagePath)) {
                Files.createDirectories(imagePath);
            }
            // 지정 경로에 파일 복사(저장)
            Files.copy(image.getInputStream(), imagePath.resolve(fileName));
            return "/images/users/" + fileName;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    /* 회원가입
    RequestDTO로 회원 정보(이메일, 비밀번호, 닉네임)를 가져오고, imageFile을 받음
    중복 확인, 암호화 과정을 거친 뒤 사용자 생성 및 저장함
    */
    @Transactional
    public SignupResponse signup(SignupRequest request, MultipartFile imageFile) {
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
        // 프로필 이미지 저장
        String profileUrl = saveImageToServer(imageFile);

        // 사용자 생성 및 저장
        User user = new User(
                request.email(),
                encodedPassword,
                request.nickname(),
                profileUrl
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
                .orElseThrow(() -> new CustomException(ErrorCode.PASSWORD_MISMATCH));
        user.updatePassword(request.password());
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

