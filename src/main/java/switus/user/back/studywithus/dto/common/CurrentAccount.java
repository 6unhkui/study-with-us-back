package switus.user.back.studywithus.dto.common;

import lombok.Builder;
import lombok.*;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.account.AccountRole;
import switus.user.back.studywithus.domain.account.AuthProvider;

@Data @NoArgsConstructor
public class CurrentAccount {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String profileImg;
    private AuthProvider provider;
    private AccountRole role;

    @Builder
    public CurrentAccount(Long id, String name, String email, String password, String profileImg, AuthProvider provider, AccountRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImg = profileImg;
        this.provider = provider;
        this.role = role;
    }

    public CurrentAccount(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.profileImg = account.getProfileImg();
        this.provider = account.getProvider();
        this.role = account.getRole();
    }

    public Account toEntity() {
        return Account.builder().id(id).name(name).email(email).password(password).profileImg(profileImg).provider(provider).role(role).build();
    }
}
