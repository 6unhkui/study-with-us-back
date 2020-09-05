package switus.user.back.studywithus.common.security.ouath2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import switus.user.back.studywithus.common.security.CustomUserDetails;
import switus.user.back.studywithus.domain.user.User;
import switus.user.back.studywithus.repository.UserRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인 진행중인 서비스를 구분하는 코드
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 진행 시 키가 되는 필드 값. Primary Key와 같은 의미임
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 CustomUserDetails에 담는다.
        CustomUserDetails userDetails = CustomUserDetails.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        saveOrUpdate(userDetails);

        return userDetails;
    }

    private User saveOrUpdate(CustomUserDetails customUserDetails) {
//        // 이메일과 일치하는 사용자를 검색하고, 존재한다면 사용자의 이름이나 프로필 사진이 변경될 때를 대비해 update 시켜줌
//        User user = userRepository.findByEmail(customUserDetails.getEmail())
//                                   .map(entity -> entity.update(customUserDetails.getName(), customUserDetails.getProfileImg()))
//                                   .orElse(customUserDetails.toEntity());

        User user = userRepository.findByEmail(customUserDetails.getEmail())
                                  .orElse(customUserDetails.toEntity());
        return userRepository.save(user);
    }
}
