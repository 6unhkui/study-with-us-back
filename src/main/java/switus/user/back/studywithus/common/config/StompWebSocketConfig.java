package switus.user.back.studywithus.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import switus.user.back.studywithus.common.error.exception.UnauthorizedException;
import switus.user.back.studywithus.common.security.CustomUserDetails;
import switus.user.back.studywithus.common.security.JwtTokenProvider;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.chat.ChatMember;
import switus.user.back.studywithus.domain.chat.ChatMessage;
import switus.user.back.studywithus.dto.AccountDto;
import switus.user.back.studywithus.dto.ChatMessageDto;
import switus.user.back.studywithus.service.ChatService;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatService chatService;

    // 클라이언트가 메시지를 구독할 endpoint
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // 메시지 브로커에게 라우팅됨
        registry.setApplicationDestinationPrefixes("/pub"); //  annotated method 로 라우팅
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatting").setAllowedOrigins("*").withSockJS();
    }


    // 인바운드 채널에 인터셉터를 추가하여 검증한다.
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                switch (accessor.getCommand()){
                    case CONNECT: { // websocket connection 요청
                        // connection 시, 토큰을 검증한다.
                        String token = jwtTokenProvider.resolveToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")));
                        if(!jwtTokenProvider.validateToken(token)) {
                            throw new UnauthorizedException("인증되지 않은 사용자 요청입니다.");
                        }
                        break;
                    }
                    case SUBSCRIBE: { // 채팅룸 구독요청
                        // header에 담긴 토큰으로 유저 정보를 얻는다.
                        String token = jwtTokenProvider.resolveToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")));
                        Authentication authentication = jwtTokenProvider.getAuthentication(token);
                        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                        ChatMember chatMember = new ChatMember(userDetails.getAccount());

                        String sessionId = (String) message.getHeaders().get("simpSessionId");

                        // header에서 구독 destination 정보를 얻고, roomId를 추출한다.
                        Long roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));

                        // 입장한 클라어인트를 추가한다.
                        chatService.addChatMember(roomId, sessionId, chatMember);

                        // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
                        ChatMessageDto messageDto = ChatMessageDto.builder().type(ChatMessage.Type.ENTER).roomId(roomId).sender(chatMember).build();
                        chatService.sendChatMessage(messageDto);

                        // roomId를 세션 속성값으로 넣어준다.
                        accessor.getSessionAttributes().put("roomId", roomId);
                        log.info("SUBSCRIBED {}, {}", userDetails.getAccount().getName(), roomId);
                        break;
                    }
                    case DISCONNECT: { // websocket disconnect
                        String sessionId = (String) message.getHeaders().get("simpSessionId");

                        // 세션 속성 값에서 roomId를 가져온다.
                        Long roomId = (Long) accessor.getSessionAttributes().get("roomId");

                        // sessionId와 roomId로 퇴장하는 클라이언트 정보를 얻는다.
                        ChatMember chatMember = chatService.getChatMember(roomId, sessionId);

                        // 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
                        ChatMessageDto messageDto = ChatMessageDto.builder().type(ChatMessage.Type.QUIT).roomId(roomId).sender(chatMember).build();
                        chatService.sendChatMessage(messageDto);

                        // 퇴장하는 클라이언트 정보를 redis에서 삭제한다.
                        chatService.removeChatMember(roomId, sessionId);

                        log.info("DISCONNECTED {}, {}", sessionId, roomId);
                        break;
                    }
                    default: {}
                }
                return message;
            }
        });
    }
}
