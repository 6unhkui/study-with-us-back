package switus.user.back.studywithus.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.member.RoomMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Where(clause = "del_Flag = false")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50)
    private String phoneNo;

    @Column(columnDefinition = "Blob")
    private String profileImg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL;


    @OneToMany(mappedBy = "user")
    private List<RoomMember> roomMembers = new ArrayList<>();


    @Builder
    public User(String email, String password, String name, String phoneNo, String profileImg, UserRole role, AuthProvider provider) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNo = phoneNo;
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
