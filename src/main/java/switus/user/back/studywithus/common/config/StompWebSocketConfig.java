package switus.user.back.studywithus.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
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
import switus.user.back.studywithus.common.security.JwtTokenProvider;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.chat.ChatMessage;
import switus.user.back.studywithus.dto.AccountDto;
import switus.user.back.studywithus.service.ChatService;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

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
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
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

                if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
                    String token = jwtTokenProvider.resolveToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")));
                    jwtTokenProvider.validateToken(token);

                } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
                    String token = jwtTokenProvider.resolveToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")));
                    Account account = jwtTokenProvider.parse(token);

                    // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
                    Long roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
                    System.out.println(roomId);

                    // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
                    String sessionId = (String) message.getHeaders().get("simpSessionId");
                    chatService.setUserEnterInfo(sessionId, roomId);

                    // 채팅방의 인원수를 +1한다.
                    chatService.plusUserCount(roomId);

                    // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
                    chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.Type.ENTER).roomId(roomId).sender(new AccountDto.Response(account)).build());
                    log.info("SUBSCRIBED {}, {}", account.getName(), roomId);
                } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
                    System.out.println(message.toString());
                    // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
                    String sessionId = (String) message.getHeaders().get("simpSessionId");
                    Long roomId = chatService.getUserEnterRoomId(sessionId);

//                    // 채팅방의 인원수를 -1한다.
                    chatService.minusUserCount(roomId);

                    // 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
                    String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
                    chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.Type.QUIT).roomId(roomId).sender(new AccountDto.Response(Account.builder().name(name).build())).build());
//                    // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
                    chatService.removeUserEnterInfo(sessionId);
//                    log.info("DISCONNECTED {}, {}", sessionId, roomId);
                }
                return message;
            }
        });
    }
}
