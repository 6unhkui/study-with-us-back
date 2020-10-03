package switus.user.back.studywithus.domain.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import switus.user.back.studywithus.dto.AccountDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 6494678977089006639L;

    public enum Type {
        ENTER, QUIT, TALK
    }

    private Long roomId;
    private Type type;
    private AccountDto.Response sender;
    private String message;
    private long userCount;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime timestamp;
}
