package homeTry.tag.productTag;

import homeTry.common.auth.jwt.JwtAuth;
import homeTry.member.dto.MemberDTO;
import homeTry.member.model.entity.Member;
import homeTry.member.repository.MemberRepository;
import homeTry.tag.productTag.model.entity.ProductTag;
import homeTry.tag.productTag.repository.ProductTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductTagTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductTagRepository productTagRepository;

    @Autowired
    private JwtAuth jwtAuth;

    private String token;
    private Member member;
    private Member savedMember;

    private ProductTag savedProductTag;

    @BeforeEach
    void beforeEach() {

        member = new Member("test@test.com", "1234");
        member.promoteToAdmin();
        savedMember = memberRepository.save(member);
        token = jwtAuth.generateToken(MemberDTO.from(savedMember));

        savedProductTag = productTagRepository.save(new ProductTag("testTagName"));
    }

    @Test
    @DisplayName("상품 태그 목록 조회 테스트")
    void getProductTagListTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/productTag")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
    }


    @Test
    @DisplayName("상품 태그 생성 테스트")
    void createProductTagTest() throws Exception {

        String productTagRequestJson = """
            {
                "productTagName" : "createTag"
            }
            """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/productTag")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productTagRequestJson))
                        .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("상품 태그 삭제 테스트")
    void deleteProductTagTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/productTag/" + savedProductTag.getId())
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isNoContent());
    }
}
