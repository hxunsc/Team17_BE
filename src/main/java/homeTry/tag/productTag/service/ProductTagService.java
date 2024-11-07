package homeTry.tag.productTag.service;

import java.util.List;

import homeTry.member.dto.MemberDTO;
import homeTry.member.service.MemberService;
import homeTry.tag.exception.badRequestException.ForbiddenTagAccessException;
import homeTry.tag.model.vo.TagName;
import homeTry.tag.productTag.dto.ProductTagDto;
import homeTry.tag.productTag.dto.request.ProductTagRequest;
import homeTry.tag.productTag.dto.response.ProductTagResponse;
import homeTry.tag.productTag.exception.BadRequestException.ProductTagAlreadyExistsException;
import homeTry.tag.productTag.exception.BadRequestException.ProductTagNotFoundException;
import homeTry.tag.productTag.model.entity.ProductTag;
import homeTry.tag.productTag.repository.ProductTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductTagService {

    private final ProductTagRepository productTagRepository;
    private final MemberService memberService;

    public ProductTagService(ProductTagRepository productTagRepository, MemberService memberService) {
        this.productTagRepository = productTagRepository;
        this.memberService = memberService;
    }

    public List<ProductTagDto> getTeamTagList() {

        List<ProductTag> productTags = productTagRepository.findAllByIsDeprecatedFalse();

        return productTags
                .stream()
                .map(ProductTagDto::from)
                .toList();
    }

    public ProductTagResponse getProductTagResponse(MemberDTO memberDTO) {

        verifyAdmin(memberDTO);

        List<ProductTagDto> productTagList = productTagRepository.findAllByIsDeprecatedFalse()
                .stream()
                .map(ProductTagDto::from)
                .toList();

        return new ProductTagResponse(productTagList);
    }

    @Transactional
    public void addProductTag(ProductTagRequest productTagRequest, MemberDTO memberDTO) {

        verifyAdmin(memberDTO);

        if(productTagRepository.existsByTagName(new TagName(productTagRequest.productTagName()))){
            throw new ProductTagAlreadyExistsException();
        }

        productTagRepository.save(
                new ProductTag(
                        productTagRequest.productTagName())
        );
    }

    @Transactional
    public void deleteProductTag(Long productTagId, MemberDTO memberDTO) {

        verifyAdmin(memberDTO);

        ProductTag productTag = productTagRepository.findById(productTagId)
                .orElseThrow(() -> new ProductTagNotFoundException());

        productTag.markAsDeprecated();
    }

    private void verifyAdmin(MemberDTO memberDTO) {
        if (!memberService.isAdmin(memberDTO.id())) {
            throw new ForbiddenTagAccessException();
        }
    }
}
