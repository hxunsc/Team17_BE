package homeTry.product.controller;

import homeTry.common.annotation.LoginMember;
import homeTry.member.dto.MemberDTO;
import homeTry.product.dto.request.ProductRequest;
import homeTry.product.dto.response.ProductAdminResponse;
import homeTry.product.service.AdminProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/product")
public class AdminProductController {

    private final AdminProductService adminProductService;

    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    // 상품 추가
    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductRequest productRequest,
        @LoginMember MemberDTO memberDTO) {
        adminProductService.createProduct(productRequest, memberDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId,
        @LoginMember MemberDTO memberDTO) {
        adminProductService.deleteProduct(productId, memberDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 상품 조회
    @GetMapping
    public ResponseEntity<Page<ProductAdminResponse>> getProducts(Pageable pageable,
        @LoginMember MemberDTO memberDTO) {
        Page<ProductAdminResponse> products = adminProductService.getProducts(pageable, memberDTO);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
