package switus.user.back.studywithus.common.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import switus.user.back.studywithus.common.config.security.dto.CustomUserDetails;
import switus.user.back.studywithus.common.config.security.filter.CORSFilter;
import switus.user.back.studywithus.common.config.security.filter.JwtAuthenticationFilter;
import switus.user.back.studywithus.common.config.security.service.CustomUserDetailService;

import java.util.Arrays;

@RequiredArgsConstructor
@EnableWebSecurity // spring security 설정들을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomerAuthenticationEntryPoint customerAuthenticationEntryPoint;
    private final CustomUserDetailService userDetailsService;

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
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.httpBasic().disable() // Rest API 이므로, 비인증시 로그인 폼으로 리다이렉트 되는 기본 설정을 사용하지 않는다.
           .csrf().disable() // Rest API 이므로, csrf 보안이 필요없음
           .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Session을 사용하지 않음
           .and().authorizeRequests()
               .antMatchers("/*/*/auth/**").permitAll() // 인증(로그인, 회원가입) API는 모두 접근 가능
               .antMatchers("/oauth2/**").permitAll() // OAth2에 대한 요청은 모두 접근 가능
               .anyRequest().authenticated()  // 그 외 요청은 인증이 필요하다
           .and()
               .exceptionHandling().authenticationEntryPoint(customerAuthenticationEntryPoint);

        // Custom Filter
        http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class) // cors 필터 등록
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);// JWT Token 필터를 id,password 인증 필터 전에 동작하도록 설정
    }


    // 정적 컨텐츠는 Security 대상에서 제외한다.
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }
}
