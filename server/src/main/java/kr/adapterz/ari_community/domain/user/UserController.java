package kr.adapterz.ari_community.domain.user;

import kr.adapterz.ari_community.domain.user.dto.request.UpdatePasswordRequest;
import kr.adapterz.ari_community.domain.user.dto.request.UpdateUserRequest;
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
    public ResponseEntity<User> updateUser(@PathVariable Integer user_id, @RequestBody UpdateUserRequest updateUserRequest) {
        User updatedUser = userService.updateUser(user_id, updateUserRequest);
        return ResponseEntity.ok(updatedUser);
    }

    /* 비밀번호 변경
    PathVariable로 user_id를, RequestBody로 DTO 요소들을 가져옴
    비밀번호와 비밀번호 확인이 일치하는지 검증 후 service에 전달
    RequestDTO 요소: 비밀번호, 비밀번호 확인
     */
    @PatchMapping("/{user_id}")
    public ResponseEntity<?> updatePassword(@PathVariable Integer user_id, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        if (updatePasswordRequest.getPassword() == updatePasswordRequest.getPassword_check()) {
            User updatedPassword = userService.updatePassword(user_id, updatePasswordRequest.getPassword());
            return ResponseEntity.ok(updatedPassword);
        }
        else {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않음");
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer user_id) {
        userService.deleteUser(user_id);
        return ResponseEntity.ok("204 삭제 완료");
    }

}
