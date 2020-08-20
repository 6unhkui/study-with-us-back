package switus.user.back.studywithus.payload.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import switus.user.back.studywithus.domain.user.User;

@Getter @Setter
@NoArgsConstructor
public class UserSaveRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNo;
    private String profileImg;

    public User toEntity(){
        return User.builder().email(email).password(password).name(name).phoneNo(phoneNo).profileImg(profileImg).build();
    }

    @Override
    public String toString() {
        return "UserSaveRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", profileImg='" + profileImg + '\'' +
                '}';
    }
}
