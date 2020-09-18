package switus.user.back.studywithus.common.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import switus.user.back.studywithus.common.properties.AppAccountProperties;
import switus.user.back.studywithus.common.security.constant.SecurityConstants;
import switus.user.back.studywithus.common.error.exception.CommonRuntimeException;
import switus.user.back.studywithus.common.error.exception.InternalServerException;
import switus.user.back.studywithus.common.error.exception.InvalidTokenException;
import switus.user.back.studywithus.domain.account.AccountRole;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;


/**
 * JWT 토큰 생성 및 유효성 검증을 수행하는 컴포넌트
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Autowired
    private CustomUserDetailService userDetailsService;

    @Autowired
    private AppAccountProperties appProperties;


    @Value("${app.auth.token-secret}")
    private String secretKey;


    @PostConstruct
    protected void init() {
        // 프로퍼티에 설정한 key값을 Base64 방식으로 인코딩 하고 그 값을 secretKey로 사용한다.
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    // 토큰 생성
    public String generate(String email, AccountRole role){
        Date now = new Date();
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .setHeaderParam("type", SecurityConstants.TOKEN_TYPE) // 토큰 타입
                .setIssuer(SecurityConstants.TOKEN_ISSUER) // 토큰 발급자
                .setAudience(SecurityConstants.TOKEN_AUDIENCE) // 토큰 대상자
                .setSubject(SecurityConstants.TOKEN_SUBJECT_PREFIX + email) // 토큰 제목
                .claim(SecurityConstants.TOKEN_CLAIM_KEY_USER_ID, email) // 토큰 데이터 - email
                .claim(SecurityConstants.TOKEN_CLAIM_KEY_USER_TYPE, role) // 토큰 데이터 - role
                .setIssuedAt(now) // 토큰 발행 일자
                .setExpiration(new Date(now.getTime() + SecurityConstants.TOKEN_VALID_MILISECOND)) // 토큰 만료 일자
                .compact();
    }


    // 토큰 유효성 체그
    public boolean validateToken(String token) throws SignatureException {
        Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }


    // 토큰 파싱
    public String parse(String token){
        CommonRuntimeException ex;

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

//            CurrentUser currentUser = CurrentUser.builder()
//                    .id(parseUserId(claims)).build();

            return parseUserId(claims);

        } catch (SignatureException e){
            log.trace("[Invalid JWT signature]\n {}", e);
            ex = new InvalidTokenException(e.getMessage());

        } catch (MalformedJwtException e){
            log.trace("[Invalid JWT token]\n {}", e);
            ex = new InvalidTokenException(e.getMessage());

        } catch (ExpiredJwtException e) {
            log.trace("[Expired JWT token]\n {}", e);
            ex = new InvalidTokenException(e.getMessage());

        } catch (UnsupportedJwtException e) {
            log.trace("[Unsupported JWT token]\n {}", e);
            ex = new InvalidTokenException(e.getMessage());

        } catch (RequiredTypeException e) {
            log.trace("[JWT token claims are invalid]\n {}", e);
            ex = new InvalidTokenException("[RequiredTypeException] " + e.getMessage());

        } catch (IllegalArgumentException e) {
            log.trace("[JWT token compact of handler are invalid]\n {}", e);
            ex = new InternalServerException("[IllegalArgumentException] " + e.getMessage());

        } catch (Exception e) {
            log.trace("[Unexpected exception trace]\n {}", e);
            ex = new InternalServerException(e.getMessage());
        }

        throw ex;
    }


    // claim에서 데이터 파싱
    private String parseUserId(Claims claims){
        if(!claims.containsKey(SecurityConstants.TOKEN_CLAIM_KEY_USER_ID)){
            throw new RequiredTypeException("UserId could not be found in claims");
        }
        return claims.get(SecurityConstants.TOKEN_CLAIM_KEY_USER_ID, String.class);
    }


    // Token을 통해 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        // Token을 파싱해서 사용자의 식별자 값을 얻고, 식별자 값을 통해 Account 엔티티를 조회한다.
        // 조회한 Account 엔티티를 Spring Security가 사용자 정보를 담아 관리하는 UserDetails 인터페이스 구현체에 넣는다.
        UserDetails userDetails = userDetailsService.loadUserByUsername(parse(token));

        // UsernamePasswordAuthenticationToken(Authentication 인터페이스의 구현체)을 생성한다.
        // 생성한 이 객체를 통해 AuthenticationManager가 인증 과정을 거침
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


    // HttpServletRequest의 Header에서 Token 값을 가져온다.
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader(HttpHeaders.AUTHORIZATION).replace(SecurityConstants.TOKEN_PREFIX,"").trim();
    }

}
