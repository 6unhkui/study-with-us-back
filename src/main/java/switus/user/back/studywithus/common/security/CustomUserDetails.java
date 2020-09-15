package switus.user.back.studywithus.common.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import switus.user.back.studywithus.domain.account.AccountRole;
import switus.user.back.studywithus.domain.account.AuthProvider;
import switus.user.back.studywithus.domain.account.Account;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 인증 시 사용할 UserDetails의 구현체
 */
@Getter @Setter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private Account account;
    // OAuth2
    private String nameAttributeKey;  // OAuth2 로그인 진행 시 키가 되는 필드 값. Primary Key와 같은 의미임
    private Map<String, Object> attributes; // Provider가 제공하는 유저의 정보 값


    public CustomUserDetails(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }


    @Builder
    public CustomUserDetails(Map<String, Object> attributes, String nameAttributeKey, Account account) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()));
    }

    @Override
    public String getPassword() {
        return this.account.getPassword();
    }

    @Override
    public String getUsername() {
        return this.account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return this.account.getName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }



    /**
     * OAuth2 Provider가 제공하는 유저 정보로 CustomUserDetails 생성하기
     * @param registrationId
     * @param userNameAttributeName
     * @param attributes
     * @return
     */
    public static CustomUserDetails of(String registrationId,
                                       String userNameAttributeName,
                                       Map<String, Object> attributes) {
        if("naver".equals(registrationId))
            return ofNaver("id", attributes);
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static CustomUserDetails ofGoogle(String userNameAttributeName,
                                             Map<String, Object> attributes) {
        return CustomUserDetails.builder()
                                .account(Account.builder()
                                        .name((String)attributes.get("name"))
                                        .email((String)attributes.get("email"))
                                        .profileImg((String)attributes.get("picture"))
                                        .provider(AuthProvider.GOOGLE)
                                        .role(AccountRole.USER).build())
                                .attributes(attributes)
                                .nameAttributeKey(userNameAttributeName)
                                .build();
    }

    public static CustomUserDetails ofNaver(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");

        return CustomUserDetails.builder()
                .account(Account.builder()
                        .name((String)response.get("name"))
                        .email((String)response.get("email"))
                        .profileImg((String)response.get("profile_image"))
                        .provider(AuthProvider.NAVER)
                        .role(AccountRole.USER).build())
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
