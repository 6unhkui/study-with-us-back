package switus.user.back.studywithus.domain.board;

import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.user.User;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class RoomBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idx;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "idx")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "idx")
    private Room room;

}

