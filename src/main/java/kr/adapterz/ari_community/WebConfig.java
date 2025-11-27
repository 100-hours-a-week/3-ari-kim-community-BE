package kr.adapterz.ari_community;

import kr.adapterz.ari_community.global.exception.FilterExceptionResolver;
import kr.adapterz.ari_community.global.jwt.JwtAuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;                                                                               
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;      

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthorizationInterceptor jwtAuthorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthorizationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**", "/api/posts", "/api/users/signup");
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(0, new FilterExceptionResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // http://localhost:3000 의 요청을 허용
                .allowCredentials(true); // (선택) 쿠키/인증 헤더 사용 시       
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/posts/** → 파일 시스템 /Documents/images/posts/ 로 매핑
        registry.addResourceHandler("/images/posts/**")
                .addResourceLocations("file:/Documents/images/posts/");

        // /images/users/** → 파일 시스템 /Documents/images/users/ 로 매핑
        registry.addResourceHandler("/images/users/**")
                .addResourceLocations("file:/Documents/images/users/");

        // FE 프로젝트의 레이아웃 파일 참조 (HTML, CSS 등)
        registry.addResourceHandler("/layout/**")
                .addResourceLocations("file:/Users/dvin/Documents/GitHub/3-ari-kim-community-FE/public/layout/");                                               

        // FE 프로젝트의 index.css 파일 참조
        registry.addResourceHandler("/index.css")
                .addResourceLocations("file:/Users/dvin/Documents/GitHub/3-ari-kim-community-FE/");                                                             
    }

}