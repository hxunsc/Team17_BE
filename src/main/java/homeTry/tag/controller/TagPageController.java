package homeTry.tag.controller;

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
    public String showProductTagList(Model model) {
        List<ProductTagDto> productTags = productTagService.getProductTagList().productTags();
        model.addAttribute("productTags", productTags);
        return "tag/ProductTags";
    }

    @GetMapping("/team")
    public String showTeamTagList(Model model) {
        List<TeamTagDTO> teamTags = teamTagService.getAllTeamTagList();
        model.addAttribute("teamTags", teamTags);
        return "tag/TeamTags";
    }

    @GetMapping("/product/add")
    public String showProductTagForm() {
        return "tag/AddProductTag";
    }

    @PostMapping("/product/save")
    public String saveProductTag(@ModelAttribute ProductTagRequest productTagRequest) {
        productTagService.addProductTag(productTagRequest);
        return "redirect:/admin/tag/product"; // 상품 태그 목록 페이지로 리다이렉트
    }

    @GetMapping("/team/add")
    public String showTeamTagFrom() {
        return "tag/AddTeamTag";
    }

    @PostMapping("/team/save")
    public String saveTeamTag(@ModelAttribute TeamTagRequest teamTagRequest) {
        teamTagService.addTeamTag(teamTagRequest);
        return "redirect:/admin/tag/team";
    }

    @DeleteMapping("/product/delete/{id}")
    public String deleteProductTag(@PathVariable Long id) {
        productTagService.deleteProductTag(id);
        return "redirect:/admin/tag/product";
    }

    @DeleteMapping("/team/delete/{id}")
    public String deleteTeamTag(@PathVariable Long id) {
        teamTagService.deleteTeamTag(id);
        return "redirect:/admin/tag/team";
    }

}