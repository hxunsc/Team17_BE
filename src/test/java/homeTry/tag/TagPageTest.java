package homeTry.tag;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import homeTry.common.auth.jwt.JwtAuth;
import homeTry.member.dto.MemberDTO;
import homeTry.member.model.entity.Member;
import homeTry.member.repository.MemberRepository;
import homeTry.tag.productTag.dto.request.ProductTagRequest;
import homeTry.tag.productTag.model.entity.ProductTag;
import homeTry.tag.productTag.repository.ProductTagRepository;
import homeTry.tag.teamTag.dto.request.TeamTagRequest;
import homeTry.tag.teamTag.model.entity.TeamTag;
import homeTry.tag.teamTag.repository.TeamTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class TagPageTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamTagRepository teamTagRepository;

    @Autowired
    private ProductTagRepository productTagRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtAuth jwtAuth;

    private String token;
    private Member member;
    private Member savedMember;

    private ProductTag savedProductTag;
    private TeamTag savedTeamTag;

    @BeforeEach
    void beforeEach() {

        member = new Member("test@test.com", "1234");
        member.promoteToAdmin();
        savedMember = memberRepository.save(member);
        token = jwtAuth.generateToken(MemberDTO.from(savedMember));

        savedProductTag = productTagRepository.save(new ProductTag("testProductTag"));
        savedTeamTag = teamTagRepository.save(new TeamTag("testTeamTag", "testAttribute"));
    }


    @Test
    @DisplayName("상품 태그 목록 페이지 요청 테스트")
    void showProductTagListTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tag/product")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(view().name("tag/ProductTags"))
                        .andExpect(model().attributeExists("productTags"));
    }

    @Test
    @DisplayName("팀 태그 목록 페이지 요청 테스트")
    void showTeamTagListTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tag/team")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(view().name("tag/TeamTags"))
                        .andExpect(model().attributeExists("teamTags"));
    }

    @Test
    @DisplayName("상품 태그 추가 폼 페이지 요청 테스트")
    void showProductTagFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tag/product/add")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(view().name("tag/AddProductTag"));
    }

    @Test
    @DisplayName("상품 태그 저장 테스트")
    void saveProductTagTest() throws Exception {
        ProductTagRequest request = new ProductTagRequest("testTagName");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/tag/product/save")
                        .header("Authorization", "Bearer " + token)
                        .flashAttr("productTagRequest", request))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/admin/tag/product"));
    }

    @Test
    @DisplayName("팀 태그 추가 폼 페이지 요청 테스트")
    void showTeamTagFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/tag/team/add")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(view().name("tag/AddTeamTag"));
    }

    @Test
    @DisplayName("팀 태그 저장 테스트")
    void saveTeamTagTest() throws Exception {

        TeamTagRequest request = new TeamTagRequest("testTagName", "testAttribute");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/tag/team/save")
                        .header("Authorization", "Bearer " + token)
                        .flashAttr("teamTagRequest", request))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/admin/tag/team"));
    }

    @Test
    @DisplayName("상품 태그 삭제 테스트")
    void deleteProductTagTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/tag/product/delete/" + savedProductTag.getId())
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/admin/tag/product"));
    }

    @Test
    @DisplayName("팀 태그 삭제 테스트")
    void deleteTeamTagTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/tag/team/delete/" + savedTeamTag.getId())
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(view().name("redirect:/admin/tag/team"));
    }
}
