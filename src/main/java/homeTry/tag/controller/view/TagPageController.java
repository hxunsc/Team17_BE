package homeTry.tag.controller.view;

import homeTry.tag.productTag.dto.ProductTagDto;
import homeTry.tag.productTag.service.ProductTagService;
import homeTry.tag.teamTag.dto.TeamTagDTO;
import homeTry.tag.teamTag.service.TeamTagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/page/tag")
public class TagPageController {

    private final TeamTagService teamTagService;
    private final ProductTagService productTagService;

    public TagPageController(TeamTagService teamTagService, ProductTagService productTagService) {
        this.teamTagService = teamTagService;
        this.productTagService = productTagService;
    }

    @GetMapping("/product")
    public String showProductTagList(Model model) {
        List<ProductTagDto> productTags = productTagService.getProductTagResponse().productTags();
        model.addAttribute("productTags", productTags);
        return "tag/ProductTags";
    }

    @GetMapping("/team")
    public String showTeamTagList(Model model) {
        List<TeamTagDTO> teamTags = teamTagService.getTeamTagResponse().teamTags();
        model.addAttribute("teamTags", teamTags);
        return "tag/TeamTags";
    }

    @GetMapping("/product/add")
    public String showProductTagForm() {
        return "tag/AddProductTag";
    }

    @GetMapping("/team/add")
    public String showTeamTagFrom() {
        return "tag/AddTeamTag";
    }

}