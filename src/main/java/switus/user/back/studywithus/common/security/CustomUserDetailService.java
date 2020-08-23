package switus.user.back.studywithus.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import switus.user.back.studywithus.common.security.CustomUserDetails;
import switus.user.back.studywithus.service.UserService;

/**
 * 인증 시 사용할 UserDetailsService의 구현체
 */
@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return CustomUserDetails.create(userService.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("존재하지 않는 회원입니다. email = " + email)));
    }
}
