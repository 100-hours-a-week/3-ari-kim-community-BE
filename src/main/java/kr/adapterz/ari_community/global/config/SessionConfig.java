package kr.adapterz.ari_community.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SessionConfig implements WebMvcConfigurer {

    private final SessionInterceptor sessionInterceptor;

    /* 인터셉터 등록
    : 특정 경로에 대해 세션 기반 인증 확인
    /users/, /posts/, /comments/ 접근 시 세션 확인
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/users/**", "/comments/**", "/posts/**")
                .excludePathPatterns("/auth/**", "/posts");
    }
    
}
