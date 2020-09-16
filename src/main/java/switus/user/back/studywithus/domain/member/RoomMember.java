package switus.user.back.studywithus.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
//import switus.user.back.studywithus.domain.account.converter.AuthProviderConverter;
import switus.user.back.studywithus.domain.common.BaseEntity;
//import switus.user.back.studywithus.domain.member.converter.RoomMemberRoleConverter;
import switus.user.back.studywithus.domain.member.converter.RoomMemberRoleConverter;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.account.Account;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Where(clause = "del_Flag = false")
@Getter @NoArgsConstructor(access = PROTECTED)
public class RoomMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Convert(converter = RoomMemberRoleConverter.class)
    @Column(columnDefinition = "TINYINT not null comment '0 : Mate / 99 : Manager'")
    private RoomMemberRole role = RoomMemberRole.MATE;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void setAccount(Account account) {
        this.account = account;
        account.getRoomMembers().add(this);
    }

    public void setRoom(Room room) {
        this.room = room;
        room.getRoomMembers().add(this);
    }

    public void setRole(RoomMemberRole role) {
        this.role = role;
    }

    public static RoomMember join(Account account, Room room, RoomMemberRole role) {
        RoomMember roomMember = new RoomMember();
        roomMember.setRole(role);
        roomMember.setAccount(account);
        roomMember.setRoom(room);
        return roomMember;
    }

}