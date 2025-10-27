package kr.adapterz.ari_community.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // 이용약관 페이지 요청
    @GetMapping("/terms")
    public String getTermsPage() {
        return "terms";
    }

    // 개인정보처리방침 페이지 요청
    @GetMapping("/privacy")
    public String getPrivacyPage() {
        return "privacy";
    }

}
