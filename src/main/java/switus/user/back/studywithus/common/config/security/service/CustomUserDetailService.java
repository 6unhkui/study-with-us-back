package switus.user.back.studywithus.common.config.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import switus.user.back.studywithus.common.config.security.dto.CustomUserDetails;
import switus.user.back.studywithus.service.UserService;

/**
 * 인증 시 사용할 Custom User Service
 */
@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CustomUserDetails(userService.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 회원입니다. email = " + email)));
    }
}
