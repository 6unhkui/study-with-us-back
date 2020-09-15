package switus.user.back.studywithus.domain.account;

import lombok.*;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.member.RoomMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Where(clause = "del_Flag = false")
@Getter @NoArgsConstructor(access = PROTECTED)
public class Account extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(columnDefinition = "Blob")
    private String profileImg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountRole role = AccountRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL;

    @OneToMany(mappedBy = "account")
    private List<RoomMember> roomMembers = new ArrayList<>();

    @Builder
    public Account(String email, String password, String name, String profileImg, AccountRole role, AuthProvider provider) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.profileImg = profileImg;
        this.role = role;
        this.provider = provider;
    }

    public void changeName(String name){ this.name = name; }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeProfileImg(String profileImg){
        this.profileImg = profileImg;
    }

}
