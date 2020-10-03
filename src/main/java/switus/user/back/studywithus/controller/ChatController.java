package switus.user.back.studywithus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import switus.user.back.studywithus.domain.chat.ChatMessage;
import switus.user.back.studywithus.service.ChatService;


@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("chat/message")
    public void message(ChatMessage message) {
        // 채팅방 인원수 세팅
        message.setUserCount(chatService.getUserCount(message.getRoomId()));

        // Websocket에 발행된 메시지를 redis로 발행(publish)
        chatService.sendChatMessage(message);
    }
}
