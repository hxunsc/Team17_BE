package homeTry.product.controller;

import homeTry.product.dto.request.ProductRequest;
import homeTry.product.service.AdminProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/page/product")
public class AdminProductRestController {

    private final AdminProductService adminProductService;

    public AdminProductRestController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    // 상품 추가
    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody @Valid ProductRequest productRequest) {

        adminProductService.addProduct(productRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<Void> editProduct(@PathVariable("productId") Long productId,
            @ModelAttribute @Valid ProductRequest productRequest) {

        adminProductService.updateProduct(productId, productRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {

        adminProductService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
