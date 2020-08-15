package switus.user.back.studywithus.domain.attendance;

import switus.user.back.studywithus.domain.BaseEntity;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.user.User;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class RoomAttendance extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long idx;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "idx")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "idx")
    private Room room;
}

