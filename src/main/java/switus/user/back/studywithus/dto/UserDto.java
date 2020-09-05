package switus.user.back.studywithus.dto;

import lombok.*;
import switus.user.back.studywithus.domain.user.AuthProvider;
import switus.user.back.studywithus.domain.user.User;
import switus.user.back.studywithus.domain.user.UserRole;
import switus.user.back.studywithus.dto.common.AccessToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserDto {

    @Getter
    @AllArgsConstructor
    public static class LoginRequest {
        @NotEmpty
        private String email;
        @NotEmpty
        private String password;
    }


    @Getter
    @AllArgsConstructor
    public static class LoginResponse {
        private String name;
        private String profileImg;
        private String email;
        private String accessToken;
        private long expiration;

        @Builder
        public LoginResponse(String name, String profileImg, String email, AccessToken accessToken) {
            this.email = email;
            this.profileImg = profileImg;
            this.name = name;
            this.accessToken = accessToken.getAccessToken();
            this.expiration = accessToken.getExpiration();
        }
    }


    @Data @Builder
    public static class SaveRequest {
        @NotEmpty @Size(max = 100) @Email
        private String email;
        @NotEmpty @Size(max = 100)
        private String password;
        @NotEmpty @Size(max = 50)
        private String name;

        public User toEntity(){
            return User.builder().email(email).password(password).name(name).role(UserRole.USER).provider(AuthProvider.LOCAL).build();
        }
    }


    @Data
    public static class UpdateRequest {
        private String name;
    }


    @Data
    public static class PasswordChangeRequest {
        @NotEmpty
        private String oldPassword;
        @NotEmpty @Size(max = 100)
        private String newPassword;
    }


    @Data
    public static class InfoResponse {
        private String name;
        private String profileImg;
        private String email;
        private UserRole role;
        private AuthProvider provider;

        public InfoResponse(User user){
            this.name = user.getName();
            this.email = user.getEmail();
            this.profileImg = user.getProfileImg();
            this.role = user.getRole();
            this.provider = user.getProvider();
        }
    }
}
