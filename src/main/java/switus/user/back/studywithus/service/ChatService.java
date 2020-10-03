package switus.user.back.studywithus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import switus.user.back.studywithus.domain.chat.ChatMessage;
import switus.user.back.studywithus.domain.chat.ChatRoom;
import switus.user.back.studywithus.domain.room.Room;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    // Redis CacheKeys
    private static final String CHAT_ROOMS = "CHAT_ROOMS"; // 채팅룸 저장
    public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, ChatRoom> hashOpsChatRoom;
    @Resource(name = "redisTemplate")
    private HashOperations<String, Long, Long> hashOpsEnterUserInfo;


    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Long> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    // 특정 채팅방 조회
    public ChatRoom findRoomById(Long roomId) {
        return hashOpsChatRoom.get(CHAT_ROOMS, roomId);
    }

    // 특정 채팅방 조회
//    public List<ChatMessage> findRoomById(Long roomId) {
//        return hashOpsChatRoom.get(CHAT_ROOMS, roomId);
//    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public ChatRoom createChatRoom(Room room)  {
        ChatRoom chatRoom =  new ChatRoom(room);
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, Long roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public Long getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션정보와 맵핑된 채팅방ID 삭제
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    // 채팅방 유저수 조회
    public long getUserCount(Long roomId) {
        return Long.parseLong(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(Long roomId) {
        return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(Long roomId) {
        return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
    }

    /**
     * destination정보에서 roomId 추출
     */
    public Long getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return Long.valueOf(destination.substring(lastIndex + 1));
        else
            return 0L;
    }


    public void sendChatMessage(ChatMessage chatMessage) {
        chatMessage.setUserCount(getUserCount(chatMessage.getRoomId()));
        chatMessage.setTimestamp(LocalDateTime.now());
        if (ChatMessage.Type.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender().getName() + "님이 방에 입장했습니다.");
        } else if (ChatMessage.Type.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender().getName() + "님이 방에서 나갔습니다.");
        }

        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }
}
