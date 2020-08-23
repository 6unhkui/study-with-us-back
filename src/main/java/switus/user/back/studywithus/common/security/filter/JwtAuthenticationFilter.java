package switus.user.back.studywithus.common.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import switus.user.back.studywithus.common.security.JwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // UsernamePasswordAuthenticationFilter가 동작하기 전, Request로 들어오는 JWT의 유효성 검증하는 역할을 함.
    // 인증 권한이 없을 경우, 원래는 로그인 폼으로 바로 redirect 시키지만 Rest API 이므로 인증 권한이 없다는 JSON을 내려줘야 한다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            final String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

            if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth); // Authentication을 보관하고 Authentication 객체를 구할 수 있도록 함
            }
        }catch (Exception e) { // header에 토큰이 없거나, 유효하지 않은 토큰을 전달 받았을 경우 대한 처리
            SecurityContextHolder.clearContext();
            log.debug("Exception " + e.getMessage(), e);
        }finally {
            chain.doFilter(request, response);
        }
    }
}
