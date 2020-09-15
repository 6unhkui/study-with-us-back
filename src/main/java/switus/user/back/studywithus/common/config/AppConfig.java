package switus.user.back.studywithus.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import switus.user.back.studywithus.common.properties.AppAccountProperties;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.domain.account.AccountRole;
import switus.user.back.studywithus.domain.account.AuthProvider;
import switus.user.back.studywithus.repository.AccountRepository;

import java.util.Arrays;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            private AccountRepository accountRepository;

            @Autowired
            private AppAccountProperties accountProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                Account user = Account.builder().email(accountProperties.getUserEmail())
                                        .name("user").password(passwordEncoder.encode(accountProperties.getUserPassword()))
                                        .role(AccountRole.USER)
                                        .provider(AuthProvider.LOCAL).build();

                Account admin = Account.builder().email(accountProperties.getAdminEmail())
                                        .name("admin").password(passwordEncoder.encode(accountProperties.getAdminPassword()))
                                        .role(AccountRole.USER)
                                        .provider(AuthProvider.LOCAL).build();

                Arrays.asList(user,admin).forEach(a -> {
                    if(!accountRepository.findByEmail(a.getEmail()).isPresent()) {
                        accountRepository.save(a);
                    }
                });
            }
        };
    }
}
