package homeTry.chatting;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import homeTry.chatting.repository.ChattingRepository;
import homeTry.common.auth.jwt.JwtAuth;
import homeTry.member.dto.MemberDTO;
import homeTry.member.model.entity.Member;
import homeTry.member.repository.MemberRepository;
import homeTry.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ChattingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ChattingRepository chattingRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private JwtAuth jwtAuth;

    private String token;
    private Member testMember;


    @BeforeEach
    void beforeEach() throws Exception {
        testMember = new Member("test@test.com", "1234");
        memberRepository.save(testMember);

        token = jwtAuth.generateToken(MemberDTO.from(testMember));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/team/join/1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        assertThat()
    }

}


