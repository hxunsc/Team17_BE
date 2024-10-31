package homeTry.product.service;

import homeTry.member.dto.MemberDTO;
import homeTry.product.dto.response.ProductResponse;
import homeTry.product.exception.badRequestException.InvalidMemberException;
import homeTry.product.model.entity.Product;
import homeTry.product.repository.ProductRepository;
import homeTry.tag.service.ProductTagService;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductTagService productTagService;

    public ProductService(ProductRepository productRepository,
        ProductTagService productTagService) {
        this.productRepository = productRepository;
        this.productTagService = productTagService;
    }

    public Slice<ProductResponse> getProducts(List<Long> tagIds, MemberDTO memberDTO,
        Pageable pageable) {

        if (memberDTO == null) {
            throw new InvalidMemberException();
        }

        // tag O -> 해당 태그에 맞는 상품들을 가격순으로 정렬
        // tag X -> 전체 상품을 가격순으로 정렬
        Slice<Product> products = (tagIds != null && !tagIds.isEmpty())
            ? getProductsByTagIds(tagIds, pageable)
            : productRepository.findAllByOrderByPriceAsc(pageable);

        return products.map(ProductResponse::from);
    }

    private Slice<Product> getProductsByTagIds(List<Long> tagIds, Pageable pageable) {
        List<Long> productIds = productTagService.getProductIdsByTagIds(tagIds);

        return productRepository.findByIdInOrderByPriceAsc(productIds, pageable);
    }

}
