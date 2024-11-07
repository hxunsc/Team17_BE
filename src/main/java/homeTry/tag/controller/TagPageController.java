package homeTry.tag.controller;

import homeTry.common.annotation.LoginMember;
import homeTry.member.dto.MemberDTO;
import homeTry.tag.productTag.dto.ProductTagDto;
import homeTry.tag.productTag.dto.request.ProductTagRequest;
import homeTry.tag.productTag.service.ProductTagService;
import homeTry.tag.teamTag.dto.TeamTagDTO;
import homeTry.tag.teamTag.dto.request.TeamTagRequest;
import homeTry.tag.teamTag.service.TeamTagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/tag")
public class TagPageController {

    private final TeamTagService teamTagService;
    private final ProductTagService productTagService;

    public TagPageController(TeamTagService teamTagService, ProductTagService productTagService) {
        this.teamTagService = teamTagService;
        this.productTagService = productTagService;
    }

    @GetMapping("/product")
    public String showProductTagList(Model model,
                                     @LoginMember MemberDTO memberDTO) {
        List<ProductTagDto> productTags = productTagService.getProductTagResponse(memberDTO).productTags();
        model.addAttribute("productTags", productTags);
        return "tag/ProductTags";
    }

    @GetMapping("/team")
    public String showTeamTagList(Model model,
                                  @LoginMember MemberDTO memberDTO) {
        List<TeamTagDTO> teamTags = teamTagService.getTeamTagResponse(memberDTO).teamTags();
        model.addAttribute("teamTags", teamTags);
        return "tag/TeamTags";
    }

    @GetMapping("/product/add")
    public String showProductTagForm() {
        return "tag/AddProductTag";
    }

    @PostMapping("/product/save")
    public String saveProductTag(
            @ModelAttribute ProductTagRequest productTagRequest,
            @LoginMember MemberDTO memberDTO) {
        productTagService.addProductTag(productTagRequest, memberDTO);
        return "redirect:/admin/tag/product"; // 상품 태그 목록 페이지로 리다이렉트
    }

    @GetMapping("/team/add")
    public String showTeamTagFrom() {
        return "tag/AddTeamTag";
    }

    @PostMapping("/team/save")
    public String saveTeamTag(
            @ModelAttribute TeamTagRequest teamTagRequest,
            @LoginMember MemberDTO memberDTO) {
        teamTagService.addTeamTag(teamTagRequest, memberDTO);
        return "redirect:/admin/tag/team";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProductTag(@PathVariable Long id,
                                   @LoginMember MemberDTO memberDTO) {
        productTagService.deleteProductTag(id, memberDTO);
        return "redirect:/admin/tag/product";
    }

    @PostMapping("/team/delete/{id}")
    public String deleteTeamTag(@PathVariable Long id,
                                @LoginMember MemberDTO memberDTO) {
        teamTagService.deleteTeamTag(id, memberDTO);
        return "redirect:/admin/tag/team";
    }

}