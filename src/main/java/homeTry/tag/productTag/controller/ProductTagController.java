package homeTry.tag.productTag.controller;

import homeTry.tag.productTag.service.ProductTagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import homeTry.tag.productTag.dto.request.ProductTagRequest;
import homeTry.tag.productTag.dto.response.ProductTagResponse;

@RestController
@RequestMapping("api/admin/productTag")
public class ProductTagController {

    private final ProductTagService productTagService;

    public ProductTagController(ProductTagService productTagService) {
        this.productTagService = productTagService;
    }

    @GetMapping
    public ResponseEntity<ProductTagResponse> getProductTagList() {
        return new ResponseEntity<>(productTagService.getProductTagResponse(), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<Void> createProductTag(
            @RequestBody ProductTagRequest productTagRequest
        ) {
        productTagService.addProductTag(productTagRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{productTagId}")
    public ResponseEntity<Void> deleteProductTag(
            @PathVariable Long productTagId
    ) {
        productTagService.deleteProductTag(productTagId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
