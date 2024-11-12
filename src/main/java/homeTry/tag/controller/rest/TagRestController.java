package homeTry.tag.controller.rest;

import homeTry.tag.productTag.dto.request.ProductTagRequest;
import homeTry.tag.productTag.service.ProductTagService;
import homeTry.tag.teamTag.service.TeamTagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Tag", description = "태그 관리자 API")
@RestController
@RequestMapping("/admin/page/tag")
public class TagRestController {

    private final ProductTagService productTagService;
    private final TeamTagService teamTagService;

    public TagRestController(ProductTagService productTagService, TeamTagService teamTagService) {
        this.productTagService = productTagService;
        this.teamTagService = teamTagService;
    }

    @PostMapping("/product")
    public ResponseEntity<Void> addProductTag(@RequestBody @Valid ProductTagRequest productTagRequest) {
        productTagService.addProductTag(productTagRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/product/{productTagId}")
    public ResponseEntity<Void> deleteProductTag(@PathVariable Long productTagId) {
        productTagService.deleteProductTag(productTagId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
