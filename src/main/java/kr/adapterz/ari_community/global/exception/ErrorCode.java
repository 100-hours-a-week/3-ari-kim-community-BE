package kr.adapterz.ari_community.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버에 오류가 발생했습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C003", "리소스 접근 권한이 없습니다."),

    // auth
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "A001", "이미 사용 중인 이메일입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "A002", "비밀번호가 일치하지 않습니다."),
    NICKNAME_DUPLICATION(HttpStatus.CONFLICT, "A003", "이미 사용 중인 닉네임입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A004", "아이디 혹은 비밀번호가 잘못되었습니다."),

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "해당 사용자를 찾을 수 없습니다."),

    // post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "해당 게시물을 찾을 수 없습니다."),

    // image
    FILE_SIZE_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "I001", "파일 최대 크기를 초과하였습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "I002", "파일 업로드에 실패하였습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
