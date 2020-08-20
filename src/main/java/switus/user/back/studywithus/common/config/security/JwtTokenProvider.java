package switus.user.back.studywithus.common.config.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import switus.user.back.studywithus.common.config.security.constant.SecurityConstants;
import switus.user.back.studywithus.common.config.security.service.CustomUserDetailService;
import switus.user.back.studywithus.common.error.exception.CommonRuntimeException;
import switus.user.back.studywithus.common.error.exception.InternalServerException;
import switus.user.back.studywithus.common.error.exception.InvalidTokenException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Autowired
    private CustomUserDetailService userDetailsService;

    @Value("spring.jwt.secretKey")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 토큰 생성
    public String generate(String email){
        Date now = new Date();
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .setHeaderParam("type", SecurityConstants.TOKEN_TYPE) //토큰 타입
                .setIssuer(SecurityConstants.TOKEN_ISSUER) //토큰 발급자
                .setAudience(SecurityConstants.TOKEN_AUDIENCE) // 토큰 대상자
                .setSubject(SecurityConstants.TOKEN_SUBJECT_PREFIX + email) // 토큰 제목
                .claim(SecurityConstants.TOKEN_CLAIM_KEY_USER_ID, email) // 토큰 데이터 - user_id
                // .claim(SecurityConstants.TOKEN_CLAIM_KEY_USER_TYPE,  accessUser.getType()) // 토큰 데이터 - type
                .setIssuedAt(now) // 토큰 발행 일자
                .setExpiration(new Date(now.getTime() + SecurityConstants.TOKEN_VALID_MILISECOND)) // set Expire Time
                .compact();
    }

    // 유효한 토큰인지 체크
    public boolean validateToken(String token) throws SignatureException {
        Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(parse(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


    public String resolveToken(HttpServletRequest req) {
        return req.getHeader(HttpHeaders.AUTHORIZATION).replace(SecurityConstants.TOKEN_PREFIX,"").trim();
    }

    /**
     * JWT 토큰 파싱
     * @param token
     * @return 파싱된 토큰에 담긴 CurrentUser 데이터
     */
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

    private String parseUserId(Claims claims){
        if(!claims.containsKey(SecurityConstants.TOKEN_CLAIM_KEY_USER_ID)){
            throw new RequiredTypeException("UserId could not be found in claims");
        }
        return claims.get(SecurityConstants.TOKEN_CLAIM_KEY_USER_ID, String.class);
    }
}
