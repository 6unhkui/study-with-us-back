package switus.user.back.studywithus.payload.user;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter @Setter
@NoArgsConstructor
public class UserLoginRequest {
    private String email;
    private String password;
}

