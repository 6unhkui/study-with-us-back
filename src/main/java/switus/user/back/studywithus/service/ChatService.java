package switus.user.back.studywithus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import switus.user.back.studywithus.domain.chat.ChatMember;
import switus.user.back.studywithus.domain.chat.ChatMessage;
import switus.user.back.studywithus.dto.ChatMessageDto;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.String.format;
import static switus.user.back.studywithus.common.constant.RedisCacheKey.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ObjectMapper objectMapper;
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    /**
     * 채팅 메시지를 리스트로 저장한다.
     * - key : CHAT_ROOM:${roomId}:MESSAGES
     * - value : ChatMessage 객체를 직렬화 한 데이터
     * % value 값으로 ChatMessage를 저장하지 않은 이유 : list로 객체를 저장할 때 역직렬화 과정에서 에러가 발생됨
     */
    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOpsMessage;

    /**
     * 현재 채팅방에 입장한 클라이언트의 sessionId를 hash key 값으로 하여 ChatMember 객체에 클라이언트 정보를 담아 저장한다.
     * - key : CHAT_ROOM:${roomId}:MEMBERS
     * - hashKey : sessionId
     * - value : ChatMember
     */
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatMember> hashOpsChatMember;


    // 입장한 클라이언트 추가
    public void addChatMember(Long roomId, String sessionId, ChatMember chatMember) {
        String key = makeKey(roomId, MEMBERS);
        hashOpsChatMember.putIfAbsent(key, sessionId, chatMember);
    }

    // 채팅 방에 접속중인 클라이언트 리스트
    public List<ChatMember> getChatMembers(Long roomId) {
        String key = makeKey(roomId, MEMBERS);
        Map<String, ChatMember> entries = hashOpsChatMember.entries(key);
        return new ArrayList<>(entries.values());
    }

    // roomId와 sessionId로 현재 접속중인 클라이언트 정보를 얻음
    public ChatMember getChatMember(Long roomId, String sessionId) {
        String key = makeKey(roomId, MEMBERS);
        return hashOpsChatMember.get(key, sessionId);
    }

    // 퇴장한 클라이언트 리스트에서 삭제
    public void removeChatMember(Long roomId, String sessionId) {
        String key = makeKey(roomId, MEMBERS);
        hashOpsChatMember.delete(key, sessionId);
    }

    // 현재 채팅 참여중인 클라이언트 수
    public Long getChatMemberCount(Long roomId) {
        String key = makeKey(roomId, MEMBERS);
        return hashOpsChatMember.size(key);
    }


    // 채팅 메시지를 직렬화하여 저장한다.
    public void setChatMessage(Long roomId, ChatMessage chatMessage) {
        try {
            String key = makeKey(roomId, MESSAGES);
            String value = objectMapper.writeValueAsString(chatMessage);
            listOpsMessage.rightPush(key, value);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // 채팅 메시지 리스트를 레디스에서 가져오고 리스트에 담긴 값을 역직렬화 하여 리턴한다.
    public List<ChatMessage> getChatMessages(Long roomId){
        String key = makeKey(roomId, MESSAGES);
        List<String> range = listOpsMessage.range(key, 0, -1);

        List<ChatMessage> chatMessages = new ArrayList<>();
        for(String s : range) {
            try {
                chatMessages.add(objectMapper.readValue(s, ChatMessage.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

       return chatMessages;
    }


    // destination 정보에서 roomId를 추출한다.
    public Long getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return Long.valueOf(destination.substring(lastIndex + 1));
        else
            return 0L;
    }

    // key 값을 생성한다.
    // CHAT_ROOM:${roomId}:${...ArgumentValue}
    public String makeKey(Long roomId, String ...value) {
        String prefix = format("%s:%s", CHAT_ROOM, roomId);
        String result = value.length > 0 ? ":" + String.join(":", value) : "";
        return prefix + result;
    }


    // 메시지 저장 및 publish
    public void sendChatMessage(ChatMessageDto chatMessage) {
        String senderName = Optional.ofNullable(chatMessage.getSender().getName()).orElse("UnknownUser");
        if (ChatMessage.Type.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(format("[%s]님이 입장했습니다", senderName));
        } else if (ChatMessage.Type.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(format("[%s]님이 퇴장했습니다.", senderName));
        }

        chatMessage.setTimestamp(LocalDateTime.now());

        // 메시지를 저장한다.
        setChatMessage(chatMessage.getRoomId(), chatMessage.toEntity());

        // 현재 접속중인 멤버 수를 publish 데이터로 함께 전달한다.
        chatMessage.setMemberCount(getChatMemberCount(chatMessage.getRoomId()));

        // 기본 채널 토픽으로 메시지를 Publish 한다.
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }
}
