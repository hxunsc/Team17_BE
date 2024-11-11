package homeTry.product.controller;

import homeTry.common.annotation.LoginMember;
import homeTry.member.dto.MemberDTO;
import homeTry.product.dto.response.ProductResponse;
import homeTry.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Market", description = "마켓 관련 API")
@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final ProductService productService;

    public MarketController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 목록 조회", description = "필터링된 상품 목록을 페이지네이션과 함께 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 목록이 성공적으로 조회됨")
    })
    @GetMapping
    public ResponseEntity<Slice<ProductResponse>> getProducts(
        @RequestParam(required = false) List<Long> tagIds,
        @LoginMember MemberDTO memberDTO,
        @PageableDefault(size = 5)
        @SortDefaults({@SortDefault(sort = "viewCount", direction = Sort.Direction.DESC),
                       @SortDefault(sort = "price", direction = Sort.Direction.ASC)})
        Pageable pageable) {
        Slice<ProductResponse> products = productService.getProducts(tagIds, memberDTO, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);  // 상태 코드 200
    }

    @Operation(summary = "상품 상세 조회", description = "상품의 조회수를 증가시키며 상품 정보를 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 목록이 성공적으로 선택되고 조회수가 증가함")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<Void> redirectProduct(@PathVariable Long productId,
                                                @LoginMember MemberDTO memberDTO) {
        productService.incrementViewCount(productId, memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
