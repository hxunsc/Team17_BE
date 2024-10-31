package homeTry.product.service;

import homeTry.product.model.entity.ProductTagMapping;
import homeTry.product.repository.ProductTagMappingRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductTagMappingService {

    private final ProductTagMappingRepository productTagMappingRepository;

    public ProductTagMappingService(ProductTagMappingRepository productTagMappingRepository) {
        this.productTagMappingRepository = productTagMappingRepository;
    }

    // 태그에 맞는 상품 반환
    @Transactional(readOnly = true)
    public List<Long> getProductIdsByTagIds(List<Long> tagIds) {
        List<ProductTagMapping> mappings = productTagMappingRepository.findByProductTagIdIn(tagIds);

        return mappings.stream()
            .map(mapping -> mapping.getProduct().getId())
            .toList();
    }

}
