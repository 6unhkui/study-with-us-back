package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import switus.user.back.studywithus.common.util.FileUtils;
import switus.user.back.studywithus.common.util.ImageUtils;
import switus.user.back.studywithus.common.util.MultilingualMessageUtils;
import switus.user.back.studywithus.common.annotaion.CurrentUser;
import switus.user.back.studywithus.dto.AccountDto;
import switus.user.back.studywithus.dto.common.CommonResponse;
import switus.user.back.studywithus.dto.common.CurrentAccount;
import switus.user.back.studywithus.service.AccountService;

@Api(tags = {"Account"})
@RestController
@RequestMapping(value = "/api/v1/account", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountApiController {

    private final FileUtils fileUtils;
    private final ImageUtils imageUtils;
    private final AccountService accountService;
    private final MultilingualMessageUtils message;


    @ApiOperation("계정 정보 조회")
    @GetMapping
    public CommonResponse<AccountDto.DetailResponse> getAccount(@ApiIgnore @CurrentUser CurrentAccount account) {
        return CommonResponse.success(new AccountDto.DetailResponse(account));
    }


    @ApiOperation(value = "계정 정보 수정")
    @PutMapping
    public CommonResponse update(@ApiIgnore @CurrentUser CurrentAccount account,
                                 @RequestBody AccountDto.UpdateRequest request) {
        accountService.update(account.getId(), request);
        return CommonResponse.success();
    }


    @ApiOperation(value = "프로필 이미지 등록", notes = "프로필 이미지를 리사이징 후 base64로 인코딩하여 저장하고, 그 문자열을 반환합니다.")
    @PostMapping("/config")
    public CommonResponse<String> uploadProfileImg(@ApiIgnore @CurrentUser CurrentAccount account,
                                                   @RequestParam("file") MultipartFile file) {
        // 빈 파일 체크
        fileUtils.isNotEmpty(file);
        // 이미지 파일 체크
        imageUtils.verifyImageFile(file);

        String base64 = imageUtils.getThumbnailAsBase64(file);
        return CommonResponse.success(accountService.uploadProfileImg(account.getId(), base64));
    }


    @ApiOperation(value = "계정 비밀번호 변경")
    @PutMapping("/password")
    public CommonResponse updatePassword(@ApiIgnore @CurrentUser CurrentAccount account,
                                         @RequestBody AccountDto.PasswordChangeRequest request) {
        accountService.updatePassword(account.getId(), request);
        return CommonResponse.success();
    }


    @ApiOperation(value = "계정 탈퇴")
    @DeleteMapping
    public CommonResponse withdraw(@ApiIgnore @CurrentUser CurrentAccount account) {
        accountService.delete(account.getId());
        return CommonResponse.success();
    }

}
