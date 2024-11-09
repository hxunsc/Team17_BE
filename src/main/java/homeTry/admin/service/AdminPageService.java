package homeTry.admin.service;

import homeTry.admin.dto.request.AdminCodeRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application-secret.properties")
public class AdminPageService {

    @Value("${admin-code}")
    private String adminPromoteCode;

    public String promoteAdmin(String inputAdminCode) {
        if (isAdmin(inputAdminCode)) {
            return "redirect:/admin";  // 관리자 승격 성공
        } else {
            return "index";  // 실패 시 인덱스 페이지로 리턴
        }
    }

    private boolean isAdmin(String inputAdminCode) {
        return adminPromoteCode.equals(inputAdminCode);
    }
}
