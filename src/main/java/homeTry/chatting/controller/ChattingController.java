package homeTry.chatting.controller;

import homeTry.chatting.dto.request.ChattingMessageRequest;
import homeTry.chatting.dto.response.ChattingMessageResponse;
import homeTry.chatting.service.ChattingService;
import homeTry.common.annotation.LoginMember;
import homeTry.member.dto.MemberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class ChattingController {

    private final SimpMessageSendingOperations template;

    private final ChattingService chattingService;

    private static final Logger logger = LoggerFactory.getLogger(ChattingController.class);


    public ChattingController(SimpMessageSendingOperations template,
            ChattingService chattingService) {
        this.template = template;
        this.chattingService = chattingService;
    }


    @GetMapping("/api/chatting/{teamId}")
    public ResponseEntity<Slice<ChattingMessageResponse>> getChatMessages(@PathVariable("teamId") Long teamId,
            @LoginMember MemberDTO memberDTO) {
        return new ResponseEntity<>(chattingService.getChattingMessageSlice(teamId, memberDTO),
                HttpStatus.OK);
    }

    //메시지 송신 및 수신, /pub 을 생략할 수 있음. 클라이언트 단에선 /pub/message로 요청
    @MessageMapping("/{teamId}")
    public void receiveMessage(@DestinationVariable("teamId") Long teamId,
            ChattingMessageRequest chattingMessageRequest, SimpMessageHeaderAccessor headerAccessor) {

        logger.debug("Message received for team {}: {}", teamId, chattingMessageRequest);

        //세션에 member 꺼내오기 (Http 컨트롤러의 ArgumentResolver랑 비슷한 역할)
        MemberDTO memberDTO = (MemberDTO) headerAccessor.getSessionAttributes().get("member");

        //db에 저장
        chattingService.saveChattingMessage(teamId, chattingMessageRequest, memberDTO);
        // 메시지를 해당 채팅방 구독자들에게 전송
        template.convertAndSend("/sub/%d".formatted(teamId), chattingMessageRequest);
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("새로운 웹소켓 연결이 생성되었습니다!");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        logger.info("웹소켓 연결이 종료되었습니다!");
    }
}
