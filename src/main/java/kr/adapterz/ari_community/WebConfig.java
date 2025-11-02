package kr.adapterz.ari_community;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // http://localhost:3000 의 요청을 허용
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") //  허용할 HTTP 메소드
                .allowedHeaders("*")    // 모든 헤더 허용
                .allowCredentials(true); // (선택) 쿠키/인증 헤더 사용 시
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploads/** → 파일 시스템 /Documents/images/ 로 매핑
        registry.addResourceHandler("/images/posts/**")
                .addResourceLocations("file:/Documents/images/posts/");
        
        // FE 프로젝트의 레이아웃 CSS 파일 참조
        registry.addResourceHandler("/layout/**")
                .addResourceLocations("file:/Users/dvin/Documents/GitHub/3-ari-kim-community-FE/public/layout/");
        
        // FE 프로젝트의 index.css 파일 참조
        registry.addResourceHandler("/index.css")
                .addResourceLocations("file:/Users/dvin/Documents/GitHub/3-ari-kim-community-FE/");
    }

}