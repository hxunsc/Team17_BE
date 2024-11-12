package homeTry.product.controller.rest;

import homeTry.product.dto.request.ProductRequest;
import homeTry.product.service.AdminProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin Product Rest", description = "상품 관리 Rest API")
public class AdminProductRestController {

    private final AdminProductService adminProductService;

    public AdminProductRestController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    // 상품 추가
    @PostMapping
    @Operation(summary = "상품 추가하기", description = "상품을 추가한다.")
    @ApiResponse(responseCode = "200", description = "상품을 성공적으로 추가함")
    public ResponseEntity<Void> addProduct(@RequestBody @Valid ProductRequest productRequest) {

        adminProductService.addProduct(productRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 상품 수정
    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정하기", description = "상품을 수정한다.")
    @ApiResponse(responseCode = "200", description = "상품을 성공적으로 수정함.")
    public ResponseEntity<Void> editProduct(@PathVariable("productId") Long productId,
            @ModelAttribute @Valid ProductRequest productRequest) {

        adminProductService.updateProduct(productId, productRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제하기", description = "상품을 삭제한다.")
    @ApiResponse(responseCode = "204", description = "상품을 성공적으로 삭제함.")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {

        adminProductService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
