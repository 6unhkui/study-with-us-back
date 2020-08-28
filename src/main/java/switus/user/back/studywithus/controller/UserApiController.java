package switus.user.back.studywithus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.hibernate.secure.spi.IntegrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import switus.user.back.studywithus.common.error.exception.InternalServerException;
import switus.user.back.studywithus.common.security.CustomUserDetails;
import switus.user.back.studywithus.payload.user.UserResponse;
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
    public ResponseEntity<UserResponse> user(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse user = UserResponse.builder().name(userDetails.getName())
                                                  .profileImg(userDetails.getProfileImg())
                                                  .build();
        return ResponseEntity.ok(user);
    }


    @ApiOperation(value = "프로필 이미지 등록", notes = "유저의 프로필 이미지를 등록합니다.")
    @PostMapping("/user/profile")
    public ResponseEntity<String> updateProfileImg(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(userService.updateProfileImg(userDetails.getIdx(), file));
        }catch (IOException e){
           throw new InternalServerException("프로필 이미지 등록 중 에러가 발생했습니다.");
        }
    }

}
