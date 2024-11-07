package homeTry.product.controller;

import homeTry.common.annotation.LoginMember;
import homeTry.member.dto.MemberDTO;
import homeTry.product.dto.request.ProductRequest;
import homeTry.product.dto.response.ProductAdminResponse;
import homeTry.product.service.AdminProductService;
import homeTry.tag.productTag.dto.response.ProductTagResponse;
import homeTry.tag.productTag.service.ProductTagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product")
public class AdminProductViewController {

    private final AdminProductService adminProductService;
    private final ProductTagService productTagService;

    public AdminProductViewController(AdminProductService adminProductService, ProductTagService productTagService) {
        this.adminProductService = adminProductService;
        this.productTagService = productTagService;
    }

    // 상품 리스트
    @GetMapping
    public String getProducts(Model model, Pageable pageable, @LoginMember MemberDTO memberDTO) {
        Page<ProductAdminResponse> products = adminProductService.getProducts(pageable, memberDTO);
        model.addAttribute("products", products);
        return "product/productList";
    }

    // 상품 추가 페이지
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("productRequest", new ProductRequest("", "", "", 0L, "", null));
        ProductTagResponse tagResponse = productTagService.getProductTagList();
        model.addAttribute("tags", tagResponse.productTags());
        return "product/productAdd";
    }

    // 상품 추가
    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductRequest productRequest, @LoginMember MemberDTO memberDTO) {
        adminProductService.addProduct(productRequest, memberDTO);
        return "redirect:/admin/product";
    }

    // 상품 삭제
    @PostMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId, @LoginMember MemberDTO memberDTO) {
        adminProductService.deleteProduct(productId, memberDTO);
        return "redirect:/admin/product";
    }

}
