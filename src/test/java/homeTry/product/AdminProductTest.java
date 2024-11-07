package homeTry.product;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import homeTry.common.auth.jwt.JwtAuth;
import homeTry.member.dto.MemberDTO;
import homeTry.member.model.entity.Member;
import homeTry.member.repository.MemberRepository;
import homeTry.product.model.entity.Product;
import homeTry.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminProductTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtAuth jwtAuth;

    @Autowired
    private ProductRepository productRepository;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void beforeEach() {
        Member adminMember = memberRepository.save(new Member("admin@example.com", "admin"));
        adminMember.promoteToAdmin();
        memberRepository.save(adminMember);
        adminToken = jwtAuth.generateToken(MemberDTO.from(adminMember));

        Member userMember = memberRepository.save(new Member("user@example.com", "user"));
        userToken = jwtAuth.generateToken(MemberDTO.from(userMember));
    }

    @Test
    @DisplayName("관리자 - 상품 추가 O")
    void testCreateProductAsAdmin() throws Exception {
        String productRequestJson = """
                {
                    "imageUrl": "https://example.com/image.jpg",
                    "productUrl": "https://example.com/product",
                    "name": "상품명",
                    "price": 10000,
                    "storeName": "스토어명",
                    "tagId": 1
                }
            """;

        ResultActions result = mockMvc.perform(post("/api/admin/product")
            .header("Authorization", "Bearer " + adminToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(productRequestJson));

        result.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("사용자 - 상품 추가 X, 권한 없음")
    void testCreateProductAsUser() throws Exception {
        String productRequestJson = """
                {
                    "imageUrl": "https://example.com/image.jpg",
                    "productUrl": "https://example.com/product",
                    "name": "상품명",
                    "price": 10000,
                    "storeName": "스토어명",
                    "tagId": 1
                }
            """;

        ResultActions result = mockMvc.perform(post("/api/admin/product")
            .header("Authorization", "Bearer " + userToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(productRequestJson));

        result.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("관리자 - 상품 삭제")
    void testDeleteProductAsAdmin() throws Exception {
        Long productId = 1L;

        ResultActions result = mockMvc.perform(delete("/api/admin/product/" + productId)
            .header("Authorization", "Bearer " + adminToken)
            .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());

        Product deletedProduct = productRepository.findById(productId).orElse(null);
        assertNotNull(deletedProduct);
        assertTrue(deletedProduct.isDeprecated());
    }

    @Test
    @DisplayName("관리자 - 상품 목록 조회")
    void testGetProductsAsAdmin() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/admin/product")
            .header("Authorization", "Bearer " + adminToken)
            .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray());
    }


}
