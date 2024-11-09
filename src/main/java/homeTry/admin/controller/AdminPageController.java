package homeTry.admin.controller;

import homeTry.admin.dto.request.AdminCodeRequest;
import homeTry.admin.service.AdminPageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminPageController {

    private final AdminPageService adminPageService;

    public AdminPageController(AdminPageService adminPageService) {
        this.adminPageService = adminPageService;
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "adminPage";
    }

    @PostMapping("/admin/promote")
    public String promoteToAdmin(@ModelAttribute AdminCodeRequest request, Model model) {

        String result = adminPageService.promoteAdmin(request.adminCode());

        if ("index".equals(result)) {
            model.addAttribute("message", "관리자 코드가 올바르지 않습니다.");
        }

        return result;
    }
}
