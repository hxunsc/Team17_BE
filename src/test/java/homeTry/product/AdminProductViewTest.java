package homeTry.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import homeTry.common.auth.jwt.JwtAuth;
import homeTry.member.dto.MemberDTO;
import homeTry.member.model.entity.Member;
import homeTry.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AdminProductViewTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtAuth jwtAuth;

    private String adminToken;

    @BeforeEach
    void setUp() {
        Member adminMember = memberRepository.save(new Member("admin@example.com", "admin"));
        adminMember.promoteToAdmin();
        memberRepository.save(adminMember);
        adminToken = jwtAuth.generateToken(MemberDTO.from(adminMember));
    }

    @Test
    @DisplayName("관리자 - 상품 목록 페이지")
    void testGetProducts() throws Exception {
        mockMvc.perform(get("/admin/product")
                .cookie(new MockCookie("adminToken", adminToken)))
            .andExpect(status().isOk())
            .andExpect(view().name("product/productList"))
            .andExpect(model().attributeExists("products"));
    }

    @Test
    @DisplayName("관리자 - 상품 추가 폼 페이지")
    void testShowAddProductForm() throws Exception {
        mockMvc.perform(get("/admin/product/add")
                .cookie(new MockCookie("adminToken", adminToken)))
            .andExpect(status().isOk())
            .andExpect(view().name("product/productAdd"))
            .andExpect(model().attributeExists("productRequest"))
            .andExpect(model().attributeExists("tags"));
    }

    @Test
    @DisplayName("관리자 - 상품 추가 요청")
    void testAddProduct() throws Exception {
        mockMvc.perform(post("/admin/product/add")
                .cookie(new MockCookie("adminToken", adminToken))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)  // Content-Type을 폼 형식으로 설정
                .param("imageUrl", "https://example.com/image.jpg")
                .param("productUrl", "https://example.com/product")
                .param("name", "상품명")
                .param("price", "10000")
                .param("storeName", "스토어명")
                .param("tagId", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/product"));
    }

    @Test
    @DisplayName("관리자 - 상품 삭제 요청")
    void testDeleteProduct() throws Exception {
        Long productId = 1L;

        mockMvc.perform(post("/admin/product/delete/{productId}", productId)
                .cookie(new MockCookie("adminToken", adminToken)))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/product"));
    }

}