package switus.user.back.studywithus.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import switus.user.back.studywithus.common.error.exception.AccountNotFoundException;
import switus.user.back.studywithus.service.AccountService;

/**
 * 인증 시 사용할 UserDetailsService의 구현체
 */
@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CustomUserDetails(
                accountService.findByEmail(email).orElseThrow(() -> new AccountNotFoundException("존재하지 않는 회원입니다. email = " + email)));
    }
}
