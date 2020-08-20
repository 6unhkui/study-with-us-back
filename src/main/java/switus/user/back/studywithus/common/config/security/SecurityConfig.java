package switus.user.back.studywithus.common.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import switus.user.back.studywithus.common.config.security.filter.CORSFilter;
import switus.user.back.studywithus.common.config.security.filter.JwtAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity // spring security 설정들을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    // 암호화에 사용하는 PasswordEncoder를 Bean으로 등록한다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Spring Security에서 인증을 담당하는 AuthenticationManager를 Bean으로 등록한다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.httpBasic().disable() // Rest API 이므로, 비인증시 로그인 폼으로 리다이렉트 되는 기본 설정을 사용하지 않는다.
           .csrf().disable() // Rest API 이므로, csrf 보안이 필요없음
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Session을 사용하지 않음
//           .and()
//               .authorizeRequests() // URL 별 권한 관리를 설정하는 옵션의 시작점
//                    .antMatchers("/*/*/auth/**").permitAll() // 원래는 여기서 로그인, 회원가입 api 접근을 허용했는데 permitAll()이 제대로 동작하지 않아 아래 configure() 메소드에서 처리함
//                    .anyRequest().authenticated() // 그 외 요청은 모두 인증
           .and()
               .addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class) // cors 필터 등록
               .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // JWT Token 필터를 id,password 인증 필터 전에 동작하도록 설정
               .exceptionHandling().authenticationEntryPoint(new CustomerAuthenticationEntryPoint());
    }


    // Spring Security 대상에서 제외
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/*/*/auth/**") // 로그인, 회원가입은 누구나 접근 가능
                      .antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**"); // 정적 컨텐츠는 Security 대상에서 제외한다.
    }
}
