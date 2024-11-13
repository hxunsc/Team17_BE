package homeTry.product.service;

import homeTry.product.model.entity.Product;
import homeTry.product.model.entity.ProductTagMapping;
import homeTry.product.repository.ProductTagMappingRepository;
import homeTry.tag.productTag.dto.ProductTagDto;
import homeTry.tag.productTag.exception.badRequestException.ProductTagNotFoundException;
import homeTry.tag.productTag.model.entity.ProductTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductTagMappingService {

    private final ProductTagMappingRepository productTagMappingRepository;

    public ProductTagMappingService(ProductTagMappingRepository productTagMappingRepository) {
        this.productTagMappingRepository = productTagMappingRepository;
    }

    public void save(ProductTagMapping mapping) {
        productTagMappingRepository.save(mapping);
    }

    // 태그에 맞는 상품 ID 리스트 반환
    @Transactional(readOnly = true)
    public List<Long> getProductIdsByTagIds(List<Long> tagIds) {
        List<ProductTagMapping> mappings = productTagMappingRepository.findByProductTagIdIn(tagIds);

        return mappings.stream()
            .map(mapping -> mapping.getProduct().getId())
            .toList();
    }

    // 상품 ID로 태그 정보 반환
    @Transactional(readOnly = true)
    public ProductTagDto getTagForProduct(Long productId) {
        ProductTagMapping mapping = productTagMappingRepository.findByProductId(productId)
            .stream()
            .findFirst()
            .orElseThrow(ProductTagNotFoundException::new);

        return ProductTagDto.from(mapping.getProductTag());
    }

    // 특정 Product와 연관된 ProductTagMapping을 조회하고, isDeprecated를 true로 설정
    @Transactional
    public void setMappingsDeprecatedByProductId(Long productId) {
        List<ProductTagMapping> mappings = productTagMappingRepository.findByProductId(productId);
        mappings.forEach(mapping -> mapping.markAsDeprecated());
        productTagMappingRepository.saveAll(mappings);
    }

    // 특정 ProductTag와 연관된 ProductTagMapping을 조회하고, isDeprecated를 true로 설정
    @Transactional
    public void setMappingsDeprecatedByTagId(Long tagId) {
        List<ProductTagMapping> mappings = productTagMappingRepository.findByProductTagId(tagId);
        mappings.forEach(mapping -> mapping.markAsDeprecated());
        productTagMappingRepository.saveAll(mappings);
    }

    @Transactional
    public void updateProductTagMapping(Product product, ProductTag newTag) {
        // 기존 매핑 조회
        List<ProductTagMapping> existingMappings = productTagMappingRepository.findByProductId(product.getId());

        // 기존 태그와 새로운 태그가 동일한 경우, 업데이트 불필요
        if (existingMappings.size() == 1 && existingMappings.get(0).getProductTag().equals(newTag)) {
            return;
        }

        // 기존 매핑 비활성화
        existingMappings.forEach(mapping -> mapping.markAsDeprecated());

        // 새로운 태그로 매핑 추가
        ProductTagMapping newMapping = new ProductTagMapping(product, newTag);
        productTagMappingRepository.save(newMapping);
    }

}
