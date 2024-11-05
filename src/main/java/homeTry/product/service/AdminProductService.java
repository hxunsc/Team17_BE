package homeTry.product.service;

import homeTry.member.dto.MemberDTO;
import homeTry.product.dto.request.ProductRequest;
import homeTry.product.dto.response.ProductAdminResponse;
import homeTry.product.exception.badRequestException.ProductNotFoundException;
import homeTry.product.model.entity.Product;
import homeTry.product.repository.ProductRepository;
import homeTry.tag.productTag.dto.ProductTagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductTagMappingService productTagMappingService;

    public AdminProductService(ProductRepository productRepository,
        ProductTagMappingService productTagMappingService) {
        this.productRepository = productRepository;
        this.productTagMappingService = productTagMappingService;
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
    public void deleteProduct(Long productId, MemberDTO memberDTO) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }
        productRepository.deleteById(productId);
    }

    // 상품 조회
    public Page<ProductAdminResponse> getProducts(Pageable pageable, MemberDTO memberDTO) {
        return productRepository.findAllNonDeprecated(pageable)
            .map(product -> {
                ProductTagDto tagDto = productTagMappingService.getTagForProduct(product.getId());
                return ProductAdminResponse.from(product, tagDto);
            });
    }


}
