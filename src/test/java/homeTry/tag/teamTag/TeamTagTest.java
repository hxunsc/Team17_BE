package homeTry.tag.teamTag;

import homeTry.common.auth.jwt.JwtAuth;
import homeTry.member.dto.MemberDTO;
import homeTry.member.model.entity.Member;
import homeTry.member.repository.MemberRepository;
import homeTry.tag.teamTag.model.entity.TeamTag;
import homeTry.tag.teamTag.repository.TeamTagRepository;
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
public class TeamTagTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamTagRepository teamTagRepository;

    @Autowired
    private JwtAuth jwtAuth;

    private String token;
    private Member testMember;

    private TeamTag savedTeamTag;

    @BeforeEach
    void beforeEach() {

        testMember = memberRepository.save( new Member("test@test.com", "1234"));
        token = jwtAuth.generateToken(MemberDTO.from(testMember));

        savedTeamTag = teamTagRepository.save(new TeamTag("testTagName", "testAttribute"));
    }

    @Test
    @DisplayName("팀 태그 목록 조회 테스트")
    void getTeamTagListTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/teamTag")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("팀 태그 생성 테스트")
    void createTeamTagTest() throws Exception {

        String teamTagRequestJson = """
            {
                "teamTagName" : "createTag",
                "teamTagAttribute" : "attribute"
            }
            """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/teamTag")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teamTagRequestJson))
                        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("팀 태그 삭제 테스트")
    void deleteTeamTagTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/teamTag/" + savedTeamTag.getId())
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isNoContent());
    }
}
