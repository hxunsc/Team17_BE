package homeTry.adminTag.productTag.service;

import java.util.List;

import org.springframework.stereotype.Service;

import homeTry.adminTag.productTag.dto.ProductTagDto;
import homeTry.adminTag.productTag.dto.request.ProductTagRequest;
import homeTry.adminTag.productTag.dto.response.ProductTagResponse;
import homeTry.adminTag.productTag.exception.BadRequestException.ProductTagNotFoundException;
import homeTry.adminTag.productTag.model.entity.ProductTag;
import homeTry.adminTag.productTag.repository.ProductTagRepository;

@Service
public class ProductTagService {
    
    private final ProductTagRepository productTagRepository;

    public ProductTagResponse getProductTagList() {
       
        List<ProductTagDto> productTagList = productTagRepository.findAll()
                .stream()
                .map(ProductTagDto::from)
                .toList();
                
        return new ProductTagResponse(productTagList);
    }

    public ProductTagService(ProductTagRepository productTagRepository) {
        this.productTagRepository = productTagRepository;
    }

    public void addProductTag(ProductTagRequest productTagRequest) {

        productTagRepository.save(
            new ProductTag(
                productTagRequest.teamTagName())
        );
    }

    public void deleteProductTag(Long productTagId) {

        ProductTag productTag = productTagRepository.findById(productTagId)
            .orElseThrow(() -> new ProductTagNotFoundException());

        productTagRepository.delete(productTag);
    }
}
