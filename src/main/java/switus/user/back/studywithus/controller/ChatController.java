package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.chat.ChatMessage;
import switus.user.back.studywithus.dto.AccountDto;
import switus.user.back.studywithus.dto.ChatMessageDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.service.AccountService;
import switus.user.back.studywithus.service.ChatService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Api(tags = {"Chatting"})
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final AccountService accountService;

    @ApiOperation("채팅방 이전 대화 목록")
    @GetMapping("/api/v1/chat/{roomId}/history")
    public CommonResponse history(@PathVariable("roomId") Long roomId) {
        return CommonResponse.success(chatService.getChatMessages(roomId));
    }


    @ApiOperation("채팅방 현재 참여 멤버 리스트")
    @GetMapping("/api/v1/chat/{roomId}/members")
    public CommonResponse currentChatMembers(@PathVariable("roomId") Long roomId) {
        return CommonResponse.success(chatService.getChatMembers(roomId));
    }


    @MessageMapping("chat/message")
    public void message(ChatMessageDto message) {
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        chatService.sendChatMessage(message);
    }
}
