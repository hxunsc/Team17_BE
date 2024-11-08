package homeTry.chatting.endpointHandler.rest;

import homeTry.chatting.dto.response.ChattingMessageResponse;
import homeTry.chatting.service.ChattingService;
import homeTry.common.annotation.LoginMember;
import homeTry.member.dto.MemberDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/team/chatting")
public class ChattingController {

    private final ChattingService chattingService;

    public ChattingController(ChattingService chattingService) {
        this.chattingService = chattingService;
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Slice<ChattingMessageResponse>> getChatMessages(
            @PathVariable("teamId") Long teamId,
            @LoginMember MemberDTO memberDTO, Pageable pageable) {

        return new ResponseEntity<>(
                chattingService.getChattingMessageSlice(teamId, memberDTO, pageable),
                HttpStatus.OK);
    }

}
