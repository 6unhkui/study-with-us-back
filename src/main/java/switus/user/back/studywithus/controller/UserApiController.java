package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.error.exception.InternalServerException;
import switus.user.back.studywithus.common.security.CustomUserDetails;
import switus.user.back.studywithus.domain.user.User;
import switus.user.back.studywithus.dto.UserDto;
import switus.user.back.studywithus.service.UserService;

import java.io.IOException;

@Api(tags = {"2. User"})
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @ApiOperation(value = "유저 정보 조회", notes = "유저의 정보를 조회합니다.")
    @GetMapping("/user")
    public ResponseEntity<UserDto.Response> user(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findByIdx(userDetails.getIdx());

        return ResponseEntity.ok(new UserDto.Response(user));
    }

    @ApiOperation(value = "프로필 이미지 등록", notes = "유저의 프로필 이미지를 등록합니다.")
    @PostMapping("/user/profile")
    public ResponseEntity<String> uploadProfileImg(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(userService.uploadProfileImg(userDetails.getIdx(), file));
        }catch (IOException e){
           throw new InternalServerException("프로필 이미지 등록 중 에러가 발생했습니다.");
        }
    }


    @ApiOperation(value = "유저 정보 수정", notes = "유저의 정보를 수정합니다.")
    @PutMapping("/user")
    public void update(@AuthenticationPrincipal CustomUserDetails userDetails,
                       @RequestBody UserDto.UpdateRequest request) {
        userService.update(userDetails.getIdx(), request);
    }


    @ApiOperation(value = "비밀번호 변경", notes = "유저의 비밀번호를 변경합니다.")
    @PutMapping("/user/password")
    public void updatePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @RequestBody UserDto.PasswordChangeRequest request) {
        userService.updatePassword(userDetails.getIdx(), request);
    }


    @ApiOperation(value = "유저 탈퇴", notes = "유저 탈퇴를 진행합니다.")
    @DeleteMapping("/user")
    public void delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUser(userDetails.getIdx());
    }

}
