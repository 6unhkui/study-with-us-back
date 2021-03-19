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
import switus.user.back.studywithus.domain.category.Category;
import switus.user.back.studywithus.repository.AccountRepository;
import switus.user.back.studywithus.repository.CategoryRepository;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

            @Autowired
            private CategoryRepository categoryRepository;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                /**
                 * 기본 유저 저장 ////////////////////////////////////
                 */
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                Account user = Account.builder().email(accountProperties.getUserEmail())
                                        .name("user")
                                        .password(passwordEncoder.encode(accountProperties.getUserPassword()))
                                        .role(AccountRole.USER)
                                        .provider(AuthProvider.LOCAL).build();

                Account admin = Account.builder().email(accountProperties.getAdminEmail())
                                        .name("admin")
                                        .password(passwordEncoder.encode(accountProperties.getAdminPassword()))
                                        .role(AccountRole.USER)
                                        .provider(AuthProvider.LOCAL).build();

                Arrays.asList(user, admin).forEach(account -> {
                    if(!accountRepository.findByEmail(account.getEmail()).isPresent()) {
                        accountRepository.save(account);
                    }
                });


                /**
                 * 카테고리 저장 ////////////////////////////////////
                 */
                List<Category> categories = categoryRepository.findAll();

                if (categories.size() == 0) {
                    categories = new ArrayList<>();
                    categories.add(Category.builder().name("외국어").build());
                    categories.add(Category.builder().name("수능").build());
                    categories.add(Category.builder().name("공무원").build());
                    categories.add(Category.builder().name("취업").build());
                    categories.add(Category.builder().name("자격증").build());
                    categories.add(Category.builder().name("개발").build());
                    categories.add(Category.builder().name("디자인").build());
                    categories.add(Category.builder().name("기타").build());
                    categoryRepository.saveAll(categories);
                }
            }
        };
    }
}
