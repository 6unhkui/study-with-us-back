package switus.user.back.studywithus.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import switus.user.back.studywithus.common.config.security.JwtTokenProvider;
import switus.user.back.studywithus.common.config.security.SecurityConstants;
import switus.user.back.studywithus.domain.user.User;
import switus.user.back.studywithus.common.payload.AccessToken;
import switus.user.back.studywithus.payload.user.UserLoginRequest;
import switus.user.back.studywithus.payload.user.UserSaveRequest;
import switus.user.back.studywithus.service.UserService;

import java.util.Optional;

@Api(tags = {"1. Auth"})
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiOperation(value = "로그인", notes = "이메일과 비밀번호를 통해 로그인을 진행합니다. 인증이 성공하면 토큰을 발급합니다.")
    @PostMapping("login")
    public ResponseEntity login(@RequestBody UserLoginRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 이메일과 일치하는 회원 데이터를 찾는다.
        User user = userService.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 주소 입니다."));

        // 비밀번호가 일치하면 토큰을 발급한다.
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(
                    AccessToken.builder().email(user.getEmail())
                                         .accessToken(jwtTokenProvider.generate(user.getEmail()))
                                         .expiration(SecurityConstants.TOKEN_VALID_MILISECOND / 1000).build());
        }

        return new ResponseEntity("Invalid Password", HttpStatus.UNAUTHORIZED);
    }


    @ApiOperation(value = "회원 가입", notes = "회원가입을 진행합니다.")
    @PostMapping("register")
    public ResponseEntity register(UserSaveRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return ResponseEntity.ok(userService.save(request));
    }


    @ApiOperation(value = "이메일 중복 체크", notes = "중복 이메일을 체크합니다.")
    @GetMapping("check-email")
    public ResponseEntity<?> validateDuplicateEmail(@RequestParam("email") String email) {
        Optional<User> user = userService.findByEmail(email);
        if(user.isPresent())
            return new ResponseEntity("Invalid Password", HttpStatus.UNAUTHORIZED);
        else return ResponseEntity.ok("");
    }





}
