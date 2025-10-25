package kr.adapterz.ari_community.domain.user;

import kr.adapterz.ari_community.domain.user.dto.request.UpdatePasswordRequest;
import kr.adapterz.ari_community.domain.user.dto.request.UpdateUserRequest;
import kr.adapterz.ari_community.domain.user.dto.response.UpdateUserResponse;
import kr.adapterz.ari_community.global.success.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /* 회원 정보 수정
    PathVariable로 user_id를, RequestBody로 DTO 요소들을 가져옴
    RequestDTO 요소: 닉네임, 프로필URL
     */
    @PatchMapping("/{user_id}")
    public ResponseEntity<ApiResponse<UpdateUserResponse>> updateUser(@PathVariable Integer userId, @RequestBody UpdateUserRequest request) {
        UpdateUserResponse updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    /* 비밀번호 변경
    PathVariable로 user_id를, RequestBody로 DTO 요소들을 가져옴
    비밀번호와 비밀번호 확인이 일치하는지 검증 후 service에 전달
    RequestDTO 요소: 비밀번호, 비밀번호 확인
     */
    @PatchMapping("/{user_id}/password")
    public ResponseEntity<ApiResponse<UpdateUserResponse>> updatePassword(@PathVariable Integer userId, @RequestBody UpdatePasswordRequest request) {
        UpdateUserResponse updatedUser = userService.updatePassword(userId, request);
        // 4. 성공 시 200 OK 응답
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "비밀번호가 변경되었습니다."));
    }

    // 회원 탈퇴
    @DeleteMapping("/{user_id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
