package homeTry.product.service;

import homeTry.member.dto.MemberDTO;
import homeTry.member.service.MemberService;
import homeTry.product.dto.request.ProductRequest;
import homeTry.product.dto.response.ProductAdminResponse;
import homeTry.product.exception.badRequestException.ProductNotFoundException;
import homeTry.product.exception.badRequestException.UnauthorizedAccessException;
import homeTry.product.model.entity.Product;
import homeTry.product.repository.ProductRepository;
import homeTry.tag.productTag.dto.ProductTagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductTagMappingService productTagMappingService;
    private final MemberService memberService;

    public AdminProductService(ProductRepository productRepository,
        ProductTagMappingService productTagMappingService, MemberService memberService) {
        this.productRepository = productRepository;
        this.productTagMappingService = productTagMappingService;
        this.memberService = memberService;
    }

    // 상품 추가
    public void createProduct(ProductRequest request, MemberDTO memberDTO) {
        Product product = new Product(
            request.imageUrl(),
            request.productUrl(),
            request.name(),
            request.price(),
            request.storeName()
        );
        productRepository.save(product);
    }

    // 상품 삭제
    @Transactional
    public void deleteProduct(Long productId, MemberDTO memberDTO) {
        // 관리자 권한 확인
        verifyAdmin(memberDTO);

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        // 연관된 ProductTagMapping soft delete
        productTagMappingService.setMappingsDeprecatedByProductId(productId);

        // 해당 Product soft delete
        product.markAsDeprecated();
        productRepository.save(product);
    }

    // 상품 조회
    public Page<ProductAdminResponse> getProducts(Pageable pageable, MemberDTO memberDTO) {
        return productRepository.findAllNonDeprecated(pageable)
            .map(product -> {
                ProductTagDto tagDto = productTagMappingService.getTagForProduct(product.getId());
                return ProductAdminResponse.from(product, tagDto);
            });
    }

    private void verifyAdmin(MemberDTO memberDTO) {
        if (!memberService.isAdmin(memberDTO.id())) {
            throw new UnauthorizedAccessException();
        }
    }


}
