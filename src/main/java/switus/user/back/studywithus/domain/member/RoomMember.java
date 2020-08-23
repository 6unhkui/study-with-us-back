package switus.user.back.studywithus.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import switus.user.back.studywithus.domain.member.converter.RoomMemberRoleConverter;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.user.User;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class RoomMember {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idx;

    @Convert(converter = RoomMemberRoleConverter.class)
    private RoomMemberRole role;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "idx")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "idx")
    private Room room;
}