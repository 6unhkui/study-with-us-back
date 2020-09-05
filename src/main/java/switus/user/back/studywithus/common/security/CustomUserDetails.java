package switus.user.back.studywithus.common.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import switus.user.back.studywithus.domain.user.AuthProvider;
import switus.user.back.studywithus.domain.user.User;
import switus.user.back.studywithus.domain.user.UserRole;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 인증 시 사용할 UserDetails의 구현체
 */
@Getter @Setter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private Long idx;
    private String name;
    private String email;
    private String password;
    private String profileImg;
    private UserRole role;

    // OAuth2
    private String nameAttributeKey;  // OAuth2 로그인 진행 시 키가 되는 필드 값. Primary Key와 같은 의미임
    private Map<String, Object> attributes; // Provider가 제공하는 유저의 정보 값
    private AuthProvider provider;

    public static CustomUserDetails create(User user) {
        return CustomUserDetails.builder()
                                .idx(user.getIdx())
                                .name(user.getName())
                                .email(user.getEmail())
                                .profileImg(user.getProfileImg())
                                .provider(user.getProvider())
                                .role(user.getRole()).build();
    }

    public static CustomUserDetails create(User user, Map<String, Object> attributes) {
        CustomUserDetails userDetails = CustomUserDetails.create(user);
        userDetails.setAttributes(attributes);
        return userDetails;
    }

    @Builder
    public CustomUserDetails(Map<String, Object> attributes, String nameAttributeKey, AuthProvider provider,
                             Long idx, String name, String email, String profileImg, UserRole role) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.provider = provider;
        this.idx = idx;
        this.name = name;
        this.email = email;
        this.profileImg = profileImg;
        this.role = role;
    }

    // User Entity 생성
    public User toEntity() {
        return User.builder().name(name).email(email).profileImg(profileImg).role(UserRole.USER).provider(provider).build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getKey()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
        return name;
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
    public static CustomUserDetails of(String registrationId, String userNameAttributeName,
                                       Map<String, Object> attributes) {
        if("naver".equals(registrationId))
            return ofNaver("id", attributes);
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static CustomUserDetails ofGoogle(String userNameAttributeName,
                                             Map<String, Object> attributes) {
        return CustomUserDetails.builder()
                .name((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .profileImg((String)attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .provider(AuthProvider.GOOGLE)
                .role(UserRole.USER)
                .build();
    }

    public static CustomUserDetails ofNaver(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");

        return CustomUserDetails.builder()
                .name((String)response.get("name"))
                .email((String)response.get("email"))
                .profileImg((String)response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .provider(AuthProvider.NAVER)
                .role(UserRole.USER)
                .build();
    }
}
