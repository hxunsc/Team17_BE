package homeTry.chatting.interceptor;

import homeTry.chatting.exception.badRequestException.InvalidChattingTokenException;
import homeTry.chatting.exception.internalServerException.NoSuchMemberInDbWithValidTokenException;
import homeTry.common.auth.jwt.JwtAuth;
import homeTry.common.auth.jwt.JwtUtil;
import homeTry.member.dto.MemberDTO;
import homeTry.member.exception.badRequestException.MemberNotFoundException;
import homeTry.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class StompInterceptor implements ChannelInterceptor {

    private final JwtAuth jwtAuth;
    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Autowired
    public StompInterceptor(JwtAuth jwtAuth, JwtUtil jwtUtil, MemberService memberService) {
        this.jwtAuth = jwtAuth;
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        //Stomp 메세지 intercept
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        handleConnectCommand(accessor);
        //todo: 추후 다른 케이스 추가하기

        return message;
    }

    private void handleConnectCommand(StompHeaderAccessor accessor) {
        if (accessor.getCommand() == StompCommand.CONNECT) {
            String bearerToken = String.valueOf(
                    accessor.getNativeHeader("Authorization").getFirst());

            if (!jwtUtil.isValidBearerToken(bearerToken))
                throw new InvalidChattingTokenException();

            String token = bearerToken.substring(7);

            MemberDTO memberDTO;

            try {
                memberDTO = memberService.getMember(jwtAuth.extractId(token));
            } catch (MemberNotFoundException e) {
                throw new NoSuchMemberInDbWithValidTokenException();
            }

            //세션에 저장
            accessor.getSessionAttributes().put("member", memberDTO);
        }
    }
}
