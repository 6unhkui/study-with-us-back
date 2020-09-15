package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import switus.user.back.studywithus.domain.member.RoomMemberRole;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.account.AccountRole;
import switus.user.back.studywithus.domain.account.AuthProvider;
import switus.user.back.studywithus.dto.common.AccessToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AccountDto {

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
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

        public Account toEntity(){
            return Account.builder().email(email).password(password).name(name).role(AccountRole.USER).provider(AuthProvider.LOCAL).build();
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DetailResponse {
        private String name;
        private String profileImg;
        private String email;
        private AccountRole role;
        private AuthProvider provider;

        public DetailResponse(Account account){
            this.name = account.getName();
            this.email = account.getEmail();
            this.profileImg = account.getProfileImg();
            this.role = account.getRole();
            this.provider = account.getProvider();
        }
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private String name;
        private String profileImg;
        private String email;
        private RoomMemberRole role;

        public Response(Account account, RoomMemberRole role){
            this.name = account.getName();
            this.email = account.getEmail();
            this.profileImg = account.getProfileImg();
            this.role = role;
        }

        public Response(Account account){
            this.name = account.getName();
            this.email = account.getEmail();
            this.profileImg = account.getProfileImg();
        }
    }
}
