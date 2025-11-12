package kr.adapterz.ari_community.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class FilterExceptionResolver implements HandlerExceptionResolver {

    private static final String FILTER_EXCEPTION_ATTRIBUTE = "filter.exception";

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Filter에서 저장한 예외를 확인
        Exception filterException = (Exception) request.getAttribute(FILTER_EXCEPTION_ATTRIBUTE);
        
        if (filterException != null) {
            // Filter 예외를 다시 던져서 DispatcherServlet이 처리하고 @RestControllerAdvice로 위임
            if (filterException instanceof CustomException) {
                throw (CustomException) filterException;
            }
            // 일반 예외는 CustomException으로 래핑
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        return null; // 다른 예외는 기본 처리
    }

    public static void setException(HttpServletRequest request, Exception exception) {
        request.setAttribute(FILTER_EXCEPTION_ATTRIBUTE, exception);
    }
}

