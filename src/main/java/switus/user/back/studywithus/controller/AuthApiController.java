package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.UnauthorizedException;
import switus.user.back.studywithus.common.error.exception.AccountNotFoundException;
import switus.user.back.studywithus.common.security.JwtTokenProvider;
import switus.user.back.studywithus.common.security.constant.SecurityConstants;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.account.AuthProvider;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.common.AccessToken;
import switus.user.back.studywithus.dto.AccountDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.service.AccountService;

import javax.validation.Valid;
import java.util.Optional;


@Api(tags = {"1. Auth"})
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AccountService accountService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MultilingualMessageUtils message;


    @ApiOperation(value = "로그인", notes = "이메일과 비밀번호를 통해 로그인을 진행합니다. 로그인을 성공하면 토큰을 응답해줍니다.")
    @PostMapping("login")
    public CommonResponse login(@Valid @RequestBody AccountDto.LoginRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 이메일과 일치하는 회원 데이터를 찾는다.
        Account account = accountService.findByEmail(
                request.getEmail()).orElseThrow(AccountNotFoundException::new);

        System.out.println(account.toString());
        if(account.getProvider() != AuthProvider.LOCAL){
            throw new BadRequestException(message.makeMultilingualMessage("socialAccount"));
        }

        // 비밀번호가 일치하면 토큰을 발급한다.
        if (passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            AccountDto.LoginResponse response = AccountDto.LoginResponse.builder()
                                                .email(account.getEmail()).name(account.getName()).profileImg(account.getProfileImg())
                                                .accessToken(
                                                        AccessToken.builder().accessToken(jwtTokenProvider.generate(account.getEmail(), account.getRole()))
                                                                .expiration(SecurityConstants.TOKEN_VALID_MILISECOND / 1000).build())
                                                .build();

            return CommonResponse.success(response);
        }

        throw new UnauthorizedException(message.makeMultilingualMessage("wrongPassword"));
    }


    @ApiOperation(value = "회원 가입", notes = "회원가입을 진행합니다.")
    @PostMapping("register")
    public CommonResponse register(@Valid @RequestBody AccountDto.SaveRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        accountService.save(request);
        return CommonResponse.success();
    }


    @ApiOperation(value = "이메일 중복 체크", notes = "중복 이메일을 체크합니다. / 중복 = false, 중복이 아님 = true")
    @GetMapping("check-email")
    public CommonResponse validateDuplicateEmail(@RequestParam("email") String email) {
        Optional<Account> user = accountService.findByEmail(email);
        if(user.isPresent())
            return CommonResponse.success(false);
        else return CommonResponse.success(true);
    }

}
