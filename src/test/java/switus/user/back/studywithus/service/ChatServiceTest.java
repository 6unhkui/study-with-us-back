//package switus.user.back.studywithus.service;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.aspectj.lang.annotation.Before;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.ListOperations;
//import org.springframework.data.redis.core.SetOperations;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.test.context.junit4.SpringRunner;
//import switus.user.back.studywithus.domain.account.Account;
//import switus.user.back.studywithus.domain.chat.ChatMember;
//import switus.user.back.studywithus.domain.chat.ChatMessage;
//import switus.user.back.studywithus.dto.AccountDto;
//import switus.user.back.studywithus.dto.common.CurrentAccount;
//
//import javax.annotation.Resource;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static java.lang.String.format;
//import static switus.user.back.studywithus.common.constant.RedisCacheKey.CHAT_ROOM;
//import static switus.user.back.studywithus.common.constant.RedisCacheKey.MESSAGES;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ChatServiceTest {
//
//    @Autowired
//    ObjectMapper om;
//
//    public static final String CHAT_ROOM = "CHAT_ROOM"; // 채팅 메세지
//    public static final String MESSAGES = "MESSAGES"; // 채팅 메세지
//    public static final String MEMBERS = "MEMBERS"; // 채팅방에 접속한 멤버들
//
//    @Resource(name = "redisTemplate")
//    private SetOperations<String, Long> setOpsEnterAccountIds;
//
//    @Resource(name = "redisTemplate")
//    private ListOperations<String, String> listOpsString;
//
//    @Resource(name = "redisTemplate")
//    private ListOperations<String, String> listOpsMessages;
//
//    @Resource(name = "redisTemplate")
//    private ValueOperations<String, List<ChatMessage>> valueOperations;
//
//    @Resource(name = "redisTemplate")
//    private HashOperations<String, String, ChatMember> hashOperations;
//
//
//    public String makeKey(Long roomId, String ...value) {
//        String prefix = format("%s:%s", CHAT_ROOM, roomId);
//        String result = value.length > 0 ? ":" + String.join(":", value) : "";
//        return prefix + result;
//    }
//
//
//    @Test
//    public void HASH_데이터_테스트() throws Exception {
//        //given
//        Long roomId = 1L;
//        String key = makeKey(roomId, MEMBERS);
//
//        //when
//        String sessionId1 = "qewqer";
//        ChatMember chatMember1 = new ChatMember(CurrentAccount.builder().name("inkyung").build());
//        hashOperations.put(key, sessionId1, chatMember1);
//
//        String sessionId2 = "dslkfdsklfd";
//        ChatMember chatMember2 = new ChatMember(CurrentAccount.builder().name("sim").build());
//        hashOperations.put(key, sessionId2, chatMember2);
//
//        //then
//        Map<String, ChatMember> entries = hashOperations.entries(key);
//        ArrayList<ChatMember> list = new ArrayList<>(entries.values());
//        for(ChatMember c : list) {
//            System.out.println(c.getName());
//        }
//    }
//
//
//    @Test
//    public void KEY_VALUE_데이터_테스트() throws Exception {
//        //given
//        Long roomId = 1L;
//        ChatMember chatMember1 = new ChatMember(CurrentAccount.builder().name("inkyung").build());
//        ChatMember chatMember2 = new ChatMember(CurrentAccount.builder().name("sim").build());
//        ChatMessage message1 = ChatMessage.builder().message("hello2").sender(chatMember1).build();
//        ChatMessage message2 = ChatMessage.builder().message("hello23").sender(chatMember2).build();
//
//        String key = format("%s:%s:%s", CHAT_ROOM, roomId, MESSAGES);
//
//        //when
//        valueOperations.append(key, om.writeValueAsString(message1));
//
//        //then
//    }
//
//
//    @Test
//    public void ObjectMapper_역직렬화_테스트() throws Exception {
//        //given
//        Long roomId = 1L;
//        String key = makeKey(roomId, MESSAGES);
//        ChatMember chatMember1 = new ChatMember(CurrentAccount.builder().name("inkyung").build());
//        ChatMember chatMember2 = new ChatMember(CurrentAccount.builder().name("sim").build());
//        ChatMessage message1 = ChatMessage.builder().message("hello2").sender(chatMember1).build();
//        ChatMessage message2 = ChatMessage.builder().message("hello23").sender(chatMember2).build();
//
//        //when
//        try {
//            String value1 = om.writeValueAsString(message1);
//            listOpsMessages.rightPush(key, value1);
//
//            String value2 = om.writeValueAsString(message2);
//            listOpsMessages.rightPush(key, value2);
//        }catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        //then
//        List<String> range = listOpsMessages.range(key, 0, -1);
//        List<ChatMessage> chatMessages = new ArrayList<>();
//        for(String s : range) {
//            chatMessages.add(om.readValue(s, ChatMessage.class));
//        }
//
//        for(ChatMessage c : chatMessages){
//            System.out.println(c.getMessage());
//        }
//    }
//}