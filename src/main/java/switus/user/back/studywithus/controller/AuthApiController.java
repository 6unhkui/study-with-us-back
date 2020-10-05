package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import switus.user.back.studywithus.common.error.exception.BadRequestException;
import switus.user.back.studywithus.common.error.exception.UnauthorizedException;
import switus.user.back.studywithus.common.error.exception.AccountNotFoundException;
import switus.user.back.studywithus.common.security.JwtTokenProvider;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.domain.account.AuthProvider;
import switus.user.back.studywithus.domain.account.Account;
import switus.user.back.studywithus.dto.AccountDto;
import switus.user.back.studywithus.dto.common.AccessToken;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.CurrentAccount;
import switus.user.back.studywithus.service.AccountService;

import javax.validation.Valid;
import java.util.Optional;


@Api(tags = {"1. Auth"})
@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthApiController {

    private final AccountService accountService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MultilingualMessageUtils message;


    @ApiOperation(value = "토큰 요청", notes = "이메일과 비밀번호를 전달받고 값이 일치하면 access token 응답")
    @PostMapping("getToken")
    public CommonResponse<AccessToken> getToken(@Valid @RequestBody AccountDto.LoginRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 이메일과 일치하는 회원 데이터를 찾는다.
        Account account = accountService.findByEmail(request.getEmail()).orElseThrow(AccountNotFoundException::new);

        // 소셜 계정이면 exception 발생
        if(account.getProvider() != AuthProvider.LOCAL){
            throw new BadRequestException(message.makeMultilingualMessage("socialAccount"));
        }

        // 비밀번호가 일치하면 토큰을 발급한다.
        if (passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            AccessToken token = AccessToken.builder().accessToken(jwtTokenProvider.generate(new CurrentAccount(account))).build();
            return CommonResponse.success(token);
        }

        throw new UnauthorizedException(message.makeMultilingualMessage("wrongPassword"));
    }



    @ApiOperation(value = "회원 가입")
    @PostMapping("register")
    public CommonResponse register(@Valid @RequestBody AccountDto.SaveRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        accountService.save(request);
        return CommonResponse.success();
    }



    @ApiOperation(value = "이메일 중복 체크", notes = "중복 = true, 중복이 아님 = false")
    @GetMapping("check-email")
    public CommonResponse<Boolean> validateDuplicateEmail(@RequestParam("email") String email) {
        Optional<Account> user = accountService.findByEmail(email);
        return CommonResponse.success(user.isPresent());
    }

}
