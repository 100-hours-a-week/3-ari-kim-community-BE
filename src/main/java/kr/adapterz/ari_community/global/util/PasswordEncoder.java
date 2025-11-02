package kr.adapterz.ari_community.global.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    /* 비밀번호 암호화
    : Password를 BCrypt로 해시 생성
    */
    public String encode(String Password) {
        return BCrypt.hashpw(Password, BCrypt.gensalt());
    }

    /* 비밀번호 검증
    : 평문 비밀번호와 암호화된 비밀번호(BCrypt 해시)를 비교
    */
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}
