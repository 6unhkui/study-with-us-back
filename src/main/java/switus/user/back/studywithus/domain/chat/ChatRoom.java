package switus.user.back.studywithus.domain.chat;

import lombok.Getter;
import lombok.Setter;
import switus.user.back.studywithus.domain.room.Room;

import java.io.Serializable;

@Getter @Setter
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private Long roomId;
    private String name;

    public ChatRoom(Room room) {
        this.roomId = room.getId();
        this.name = room.getName();
    }
}