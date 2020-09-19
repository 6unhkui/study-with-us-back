package switus.user.back.studywithus.domain.account;

import lombok.*;
import org.hibernate.annotations.Where;
import switus.user.back.studywithus.domain.account.converter.AccountRoleConverter;
import switus.user.back.studywithus.domain.account.converter.AuthProviderConverter;
import switus.user.back.studywithus.domain.common.BaseEntity;
import switus.user.back.studywithus.domain.member.Member;

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

    @Convert(converter = AccountRoleConverter.class)
    @Column(columnDefinition = "TINYINT not null comment '0 : User / 99 : Admin'")
    private AccountRole role = AccountRole.USER;

    @Convert(converter = AuthProviderConverter.class)
    @Column(columnDefinition = "CHAR not null comment 'L : Local / G : Google / N : Naver'")
    private AuthProvider provider = AuthProvider.LOCAL;

    @OneToMany(mappedBy = "account")
    private List<Member> members = new ArrayList<>();

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

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", role=" + role +
                ", provider=" + provider +
                '}';
    }
}
