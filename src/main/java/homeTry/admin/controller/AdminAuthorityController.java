package homeTry.admin.controller;

import homeTry.common.annotation.LoginMember;
import homeTry.common.auth.exception.badRequestException.InvalidTokenException;
import homeTry.member.dto.MemberDTO;
import homeTry.member.model.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminAuthorityController {

    @GetMapping
    public String getAdminAuthorityPage(@LoginMember MemberDTO memberDTO, Model model) {
        if(memberDTO == null)
            throw new InvalidTokenException();

        if(memberDTO.role() == Role.ADMIN)
            model.addAttribute("role", "admin");

        if(memberDTO.role() == Role.USER)
            model.addAttribute("role", "user");
        
        return "admin/adminAuthority";
    }
}
