package switus.user.back.studywithus.domain.chat;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.common.CurrentAccount;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ChatMember implements Serializable {
    private static final long serialVersionUID = 6494678977089006639L;

    private Long accountId;
    private String name;
    private String profileImg;
    private String email;

    public ChatMember(CurrentAccount account) {
        this.accountId = account.getId();
        this.name = account.getName();
        this.profileImg = account.getProfileImg();
        this.email = account.getEmail();
    }

}
