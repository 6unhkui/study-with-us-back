package switus.user.back.studywithus.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.account.AccountRole;
import switus.user.back.studywithus.domain.account.AuthProvider;
import switus.user.back.studywithus.domain.member.Member;
import switus.user.back.studywithus.dto.common.CurrentAccount;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AccountDto {


    @Data
    @AllArgsConstructor
    public static class LoginRequest {
        @NotEmpty
        private String email;
        @NotEmpty
        private String password;
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
            return Account.builder().email(email)
                                    .password(password)
                                    .name(name)
                                    .role(AccountRole.USER)
                                    .provider(AuthProvider.LOCAL).build();
        }
    }


    @Data
    public static class AccountUpdateRequest {
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
        private Long accountId;
        private String name;
        private String profileImg;
        private String email;
        private AccountRole role;
        private AuthProvider provider;

        public DetailResponse(CurrentAccount account){
            this.accountId = account.getId();
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
        private Long accountId;
        private String name;
        private String profileImg;
        private String email;

        public Response() {}

        public Response(Account account){
            this.accountId = account.getId();
            this.name = account.getName();
            this.email = account.getEmail();
            this.profileImg = account.getProfileImg();
        }


        @JsonCreator
        public Response(Long accountId, String name, String profileImg, String email) {
            this.accountId = accountId;
            this.name = name;
            this.profileImg = profileImg;
            this.email = email;
        }
    }
}
