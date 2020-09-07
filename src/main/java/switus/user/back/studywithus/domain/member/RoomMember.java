package switus.user.back.studywithus.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.member.converter.RoomMemberRoleConverter;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.user.User;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Where(clause = "del_Flag = false")
@NoArgsConstructor(access = PROTECTED)
public class RoomMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idx;

    @Convert(converter = RoomMemberRoleConverter.class)
    private RoomMemberRole role = RoomMemberRole.MATE;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "idx")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "idx")
    private Room room;

    public void setUser(User user) {
        this.user = user;
        user.getRoomMembers().add(this);
    }

    public void setRoom(Room room) {
        this.room = room;
        room.getRoomMembers().add(this);
    }

    public void setRole(RoomMemberRole role) {
        this.role = role;
    }

    public static RoomMember join(User user, Room room, RoomMemberRole role) {
        RoomMember roomMember = new RoomMember();
        roomMember.setRole(role);
        roomMember.setUser(user);
        roomMember.setRoom(room);
        return roomMember;
    }

}