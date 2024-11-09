package homeTry.common.interceptor;

import homeTry.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {


    public AdminInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String adminToken = getAdminTokenFromCookies(request);

        // 관리자 토큰이 없으면 Unauthorized 반환
        if (adminToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }

        // 관리자 토큰 검증
        if (isValidAdminToken(adminToken)) {
            return true;
        }

        // 유효하지 않은 관리자 토큰
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
        return false;
    }

    // 쿠키에서 adminToken을 가져오는 메서드
    private String getAdminTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("adminToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 관리자 토큰이 유효한지 확인하는 메서드
    private boolean isValidAdminToken(String adminToken) {
        return adminToken.contains("adminCheck");
    }
}