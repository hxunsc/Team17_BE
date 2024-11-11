package homeTry.admin.controller;

import homeTry.admin.dto.request.AdminCodeRequest;
import homeTry.admin.service.AdminPageService;
import homeTry.common.annotation.LoginMember;
import homeTry.member.dto.MemberDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    private final AdminPageService adminPageService;

    public AdminPageController(AdminPageService adminPageService) {
        this.adminPageService = adminPageService;
    }

    @GetMapping("/page")
    public String getAdminPage() {
        return "admin/adminPage";
    }

    @GetMapping("/promote")
    public String getAdminPromotePage() {
        return "admin/adminPromote";
    }

    @PutMapping("/promote")
    public ResponseEntity<Void> promoteToAdmin(
            @RequestBody AdminCodeRequest adminCodeRequest, @LoginMember MemberDTO memberDTO) {
        adminPageService.promoteAdmin(adminCodeRequest, memberDTO.id());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
