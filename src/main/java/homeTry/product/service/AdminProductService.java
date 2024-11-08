package homeTry.product.service;

import homeTry.member.dto.MemberDTO;
import homeTry.member.service.MemberService;
import homeTry.product.dto.request.ProductRequest;
import homeTry.product.dto.response.ProductAdminResponse;
import homeTry.product.exception.badRequestException.*;
import homeTry.product.model.entity.Product;
import homeTry.product.model.entity.ProductTagMapping;
import homeTry.product.repository.ProductRepository;
import homeTry.tag.productTag.dto.ProductTagDto;
import homeTry.tag.productTag.exception.BadRequestException.ProductTagNotFoundException;
import homeTry.tag.productTag.model.entity.ProductTag;
import homeTry.tag.productTag.repository.ProductTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductTagRepository productTagRepository;
    private final ProductTagMappingService productTagMappingService;
    private final MemberService memberService;

    public AdminProductService(ProductRepository productRepository,
        ProductTagRepository productTagRepository,
        ProductTagMappingService productTagMappingService, MemberService memberService) {
        this.productRepository = productRepository;
        this.productTagRepository = productTagRepository;
        this.productTagMappingService = productTagMappingService;
        this.memberService = memberService;
    }

    // 상품 추가
    @Transactional
    public void addProduct(ProductRequest request, MemberDTO memberDTO) {
        // 관리자 권한 확인
        verifyAdmin(memberDTO);

        if (request.tagId() == null) {
            throw new MissingProductTagException();
        }

        Product product = new Product(
            request.imageUrl(),
            request.productUrl(),
            request.name(),
            request.price(),
            request.storeName()
        );
        productRepository.save(product);

        ProductTag productTag = productTagRepository.findById(request.tagId())
            .orElseThrow(ProductTagNotFoundException::new);

        ProductTagMapping mapping = new ProductTagMapping(product, productTag);
        productTagMappingService.save(mapping);  // 태그 매핑 저장
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
    @Transactional(readOnly = true)
    public Page<ProductAdminResponse> getProducts(Pageable pageable, MemberDTO memberDTO) {
        // 관리자 권한 확인
        verifyAdmin(memberDTO);

        return productRepository.findAllNonDeprecated(pageable)
            .map(product -> {
                ProductTagDto tagDto = productTagMappingService.getTagForProduct(product.getId());
                return ProductAdminResponse.from(product, tagDto);
            });
    }

    // 관리자 권한 확인
    private void verifyAdmin(MemberDTO memberDTO) {
        if (!memberService.isAdmin(memberDTO.id())) {
            throw new ForbiddenProductAccessException();
        }
    }

}
