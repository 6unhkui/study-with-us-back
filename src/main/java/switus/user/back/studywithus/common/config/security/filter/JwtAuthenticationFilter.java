package switus.user.back.studywithus.common.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.lang.Strings;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;
import switus.user.back.studywithus.common.config.security.JwtTokenProvider;
import switus.user.back.studywithus.common.config.security.constant.SecurityConstants;
import switus.user.back.studywithus.common.config.security.service.CustomUserDetailService;
import switus.user.back.studywithus.common.error.RestRequestError;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Request로 들어오는 JWT Token의 유효성 검증하는 필터를 Filter Chain에 등록한다.
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
