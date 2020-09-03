package switus.user.back.studywithus.dto;

import lombok.*;
import switus.user.back.studywithus.domain.user.AuthProvider;
import switus.user.back.studywithus.domain.user.User;
import switus.user.back.studywithus.domain.user.UserRole;

public class UserDto {

    @Getter
    @AllArgsConstructor
    public static class LoginRequest {
        private String email;
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


    @Getter @Setter
    @Builder
    public static class SaveRequest {
        private String email;
        private String password;
        private String name;
        private UserRole role;
        private AuthProvider provider;

        public User toEntity(){
            return User.builder().email(email).password(password).name(name).role(role).provider(provider).build();
        }
    }


    @Getter
    @NoArgsConstructor
    public static class UpdateRequest {
        private String name;
    }


    @Getter @Setter
    @NoArgsConstructor
    public static class PasswordChangeRequest {
        private String oldPassword;
        private String newPassword;
    }


    @Getter
    @NoArgsConstructor
    public static class Response {
        private String name;
        private String profileImg;
        private String email;
        private UserRole role;
        private AuthProvider provider;

        public Response(User user){
            this.name = user.getName();
            this.email = user.getEmail();
            this.profileImg = user.getProfileImg();
            this.role = user.getRole();
            this.provider = user.getProvider();
        }
    }
}
