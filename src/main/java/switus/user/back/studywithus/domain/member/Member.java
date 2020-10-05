package switus.user.back.studywithus.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
//import switus.user.back.studywithus.domain.account.converter.AuthProviderConverter;
import switus.user.back.studywithus.domain.attendance.Attendance;
import switus.user.back.studywithus.domain.common.BaseEntity;
//import switus.user.back.studywithus.domain.member.converter.MemberRoleConverter;
import switus.user.back.studywithus.domain.member.converter.MemberRoleConverter;
import switus.user.back.studywithus.domain.post.Post;
import switus.user.back.studywithus.domain.room.Room;
import switus.user.back.studywithus.domain.account.Account;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Where(clause = "del_Flag = false")
@Getter @NoArgsConstructor(access = PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Convert(converter = MemberRoleConverter.class)
    @Column(columnDefinition = "TINYINT not null comment '0 : Mate / 99 : Manager'")
    private MemberRole role = MemberRole.MATE;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "member")
    private List<Post> posts;

    @OneToMany(mappedBy = "member")
    private List<Attendance> attendances;


    // == 연관 관계 편의 메소드 ==
    public void setAccount(Account account) {
        this.account = account;
        account.getMembers().add(this);
    }

    public void setRoom(Room room) {
        this.room = room;
        room.getMembers().add(this);
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }

    public static Member join(Account account, Room room, MemberRole role) {
        Member member = new Member();
        member.setRole(role);
        member.setAccount(account);

        room.incrementJoinCount();
        member.setRoom(room);
        return member;
    }

    public void withdrawal() {
        getRoom().decrementJoinCount();
        delete();
    }

}