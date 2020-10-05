package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import switus.user.back.studywithus.domain.chat.ChatMember;
import switus.user.back.studywithus.domain.chat.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class ChatMessageDto {
    public enum Type {
        ENTER, QUIT, TALK
    }

    private Long roomId;
    private ChatMessage.Type type;
    private ChatMember sender;
    private String message;
    private Long memberCount;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime timestamp;

    public ChatMessage toEntity() {
        return ChatMessage.builder().type(type).message(message).sender(sender).timestamp(timestamp).build();
    }
}
