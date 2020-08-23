package switus.user.back.studywithus.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import switus.user.back.studywithus.domain.BaseEntity;
import switus.user.back.studywithus.domain.member.RoomMember;
import switus.user.back.studywithus.domain.room.Room;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor @Getter
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50)
    private String phoneNo;

    @Column(columnDefinition = "TEXT")
    private String profileImg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL;

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

    public User update(String name, String profileImg){
        this.name = name;
        this.profileImg = profileImg;

        return this;
    }

    @OneToMany(mappedBy = "user")
    private List<RoomMember> roomMembers = new ArrayList<>();


    @Override
    public String toString() {
        return "User{" +
                "idx=" + idx +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", role=" + role +
                '}';
    }
}
