package homeTry.member;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;


import homeTry.common.auth.jwt.JwtAuth;
import homeTry.member.dto.MemberDTO;
import homeTry.member.model.entity.Member;
import homeTry.member.model.vo.Email;
import homeTry.member.model.vo.Nickname;
import homeTry.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
class MemberTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtAuth jwtAuth;

    private String token;
    private Member testMember;

    @BeforeEach
    void beforeEach() {
        testMember = new Member("test@test.com", "1234");
        memberRepository.save(testMember);

        token = jwtAuth.generateToken(MemberDTO.from(testMember));
    }

    @AfterEach
    void afterEach() {
        memberRepository.delete(testMember);
    }


    @Test
    void memberProfileTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/member/profile")
                        .header("Authorization", "Bearer " + token).contentType(
                                MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void memberNicknameChangeTest() throws Exception {
        String requestJson = """
                {"name" : "4321"}
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/member/profile")
                .header("Authorization", "Bearer " + token).content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        assertThat(memberRepository.findByEmail(new Email("test@test.com")).get()
                .getNickname()).isEqualTo(new Nickname("4321").value());
    }


}