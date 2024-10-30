package homeTry.tag.service;

import homeTry.product.model.entity.ProductTagMapping;
import homeTry.product.repository.ProductTagMappingRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductTagService {

    private ProductTagMappingRepository productTagMappingRepository;

    public ProductTagService(ProductTagMappingRepository productTagMappingRepository) {
        this.productTagMappingRepository = productTagMappingRepository;
    }

    @Transactional(readOnly = true)
    public List<Long> getProductIdsByTagIds(List<Long> tagIds) {
        List<ProductTagMapping> mappings = productTagMappingRepository.findByProductTagIdIn(tagIds);

        return mappings.stream()
            .map(ProductTagMapping::getProductId)
            .toList();
    }

}
