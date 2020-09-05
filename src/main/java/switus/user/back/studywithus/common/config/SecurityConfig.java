package switus.user.back.studywithus.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import switus.user.back.studywithus.common.security.*;
import switus.user.back.studywithus.common.security.filter.CorsFilter;
import switus.user.back.studywithus.common.security.filter.JwtAuthenticationFilter;
import switus.user.back.studywithus.common.security.ouath2.CustomOAuth2UserService;
import switus.user.back.studywithus.common.security.ouath2.HttpCookieOAuth2AuthorizationRequestRepository;
import switus.user.back.studywithus.common.security.ouath2.OAuth2AuthenticationFailureHandler;
import switus.user.back.studywithus.common.security.ouath2.OAuth2AuthenticationSuccessHandler;
import switus.user.back.studywithus.domain.user.UserRole;


@RequiredArgsConstructor
@EnableWebSecurity // spring security 설정들을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // JWT
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService userDetailsService;
    private final CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    // OAuth2
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;


    // 암호화에 사용하는 PasswordEncoder를 Bean으로 등록한다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Spring Security에서 인증을 담당하는 AuthenticationManager를 Bean에 등록한다.
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 사용자 인증에 사용하기 위해 userDetailsService의 구현체를 보안 설정하고 패스워드 인코더를 등록한다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        // Spring OAuth2는 HttpSessionOAuth2AuthorizationRequestRepository에 인증 요청을 저장한다.
        // 하지만 이 애플리케이션은 JWT를 사용하므로 Session에 저장할 필요가 없다.
        // 따라서 custom으로 구현한 HttpCookieOAuth2AuthorizationRequestRepository를 사용해 Base64 encoded cookie에 요청을 저장한다.
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.httpBasic().disable() // Rest API 이므로, 비인증시 로그인 폼으로 리다이렉트 되는 기본 설정을 사용하지 않는다.
           .csrf().disable() // Rest API 이므로, csrf 보안이 필요없음
           .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Session을 사용하지 않음
           .and()
               .authorizeRequests()
                    .antMatchers("/*/*/auth/**").permitAll() // 인증(로그인, 회원가입)은 모두 접근 가능
                    .antMatchers("/api/v1/admin/**").hasRole(UserRole.ADMIN.name()) // 특정 권한만 접근 가능. Security는 role 앞에 prefix로 "ROLE_"를 붙여 사용한다.
                    .anyRequest().authenticated()  // 그 외 요청은 인증이 필요하다
           .and()
               .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPointHandler) // 인증 에러 핸들링
                    .accessDeniedHandler(customAccessDeniedHandler) // 인가 에러 핸들링

           .and()
               .oauth2Login() // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorize")
                            .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                    .and()
                        .userInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들 담당
                            .userService(customOAuth2UserService) // 소셜 로그인 성공 시 후속 조치를 진행할 OAuth2UserService 인터페이스의 구현체
                    .and()
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler);


        // Custom Filter
        http.addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class) // cors 필터 등록
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);// JWT Token 필터를 id,password 인증 필터 전에 동작하도록 설정
    }


    // 정적 컨텐츠는 Security 대상에서 제외한다.
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }
}
