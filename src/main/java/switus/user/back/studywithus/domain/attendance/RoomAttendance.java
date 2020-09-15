package switus.user.back.studywithus.domain.attendance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.account.Account;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Where(clause = "del_Flag = false")
@Getter @NoArgsConstructor(access = PROTECTED)
public class RoomAttendance extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
}

